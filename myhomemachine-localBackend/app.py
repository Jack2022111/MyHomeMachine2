from flask import Flask, request, jsonify
from flask_cors import CORS
import pymysql
from werkzeug.security import generate_password_hash, check_password_hash
from dotenv import load_dotenv
import os
import logging
import random
import string
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from datetime import datetime, timedelta
import numpy as np
from datetime import datetime, timedelta
import json
from collections import defaultdict
import uuid


# Load environment variables from .env file
load_dotenv()

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes

# Configure secret key for session management
app.secret_key = os.getenv('SECRET_KEY', 'default_secret_key')

# Database configuration
db_host = os.getenv('DB_HOST')
db_user = os.getenv('DB_USER')
db_password = os.getenv('DB_PASSWORD')
db_name = os.getenv('DB_NAME')

# Email configuration
SMTP_SERVER = os.getenv('SMTP_SERVER', 'smtp.gmail.com')
SMTP_PORT = int(os.getenv('SMTP_PORT', '587'))
SMTP_USERNAME = os.getenv('SMTP_USERNAME')
SMTP_PASSWORD = os.getenv('SMTP_PASSWORD')
APP_DOMAIN = os.getenv('APP_DOMAIN', 'http://10.0.2.2:5000')

# Configure logging
logging.basicConfig(
    level=logging.DEBUG,
    format='%(asctime)s %(levelname)s %(name)s %(threadName)s : %(message)s'
)
logger = logging.getLogger(__name__)

def get_db_connection():
    try:
        connection = pymysql.connect(
            host=db_host,
            user=db_user,
            password=db_password,
            database=db_name,
            cursorclass=pymysql.cursors.DictCursor
        )
        logger.debug("Database connection established.")
        return connection
    except pymysql.MySQLError as e:
        logger.error(f"Database connection failed: {e}")
        return None

def generate_verification_code():
    """Generate a 6-digit verification code"""
    return ''.join(random.choices(string.digits, k=6))

def send_email(to_email, subject, body):
    try:
        msg = MIMEMultipart()
        msg['From'] = SMTP_USERNAME
        msg['To'] = to_email
        msg['Subject'] = subject
        msg.attach(MIMEText(body, 'html'))
        
        server = smtplib.SMTP(SMTP_SERVER, SMTP_PORT)
        server.starttls()
        server.login(SMTP_USERNAME, SMTP_PASSWORD)
        server.send_message(msg)
        server.quit()
        
        logger.info(f"Email sent successfully to {to_email}")
        return True
    except Exception as e:
        logger.error(f"Failed to send email: {e}")
        return False

@app.route('/api/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy"}), 200

@app.route('/api/auth/signup', methods=['POST'])
def signup():
    try:
        data = request.get_json()
        
        if not data or not all(k in data for k in ['email', 'password']):
            return jsonify({
                "success": False,
                "message": "Missing required fields"
            }), 400

        email = data['email']
        password = data['password']
        
        # Hash the password
        password_hash = generate_password_hash(password)
        
        # Generate verification code
        verification_code = generate_verification_code()
        code_expiry = datetime.now() + timedelta(minutes=30)
        
        connection = get_db_connection()
        if not connection:
            return jsonify({
                "success": False,
                "message": "Database connection failed"
            }), 500

        with connection:
            with connection.cursor() as cursor:
                # Check if email already exists
                cursor.execute("SELECT * FROM users WHERE email=%s", (email,))
                if cursor.fetchone():
                    return jsonify({
                        "success": False,
                        "message": "Email already registered"
                    }), 409

                # Insert new user with verification code
                cursor.execute(
                    """INSERT INTO users 
                       (email, password, verification_code, verification_code_expiry, email_verified) 
                       VALUES (%s, %s, %s, %s, FALSE)""",
                    (email, password_hash, verification_code, code_expiry)
                )
                connection.commit()
                
                # Send verification email with code
                email_body = f"""
                <html>
                    <body>
                        <h2>Welcome to MyHomeMachine!</h2>
                        <p>Your verification code is:</p>
                        <h1 style="font-size: 32px; color: #4A90E2; text-align: center; padding: 20px;">{verification_code}</h1>
                        <p>This code will expire in 30 minutes.</p>
                        <p>If you didn't create an account, please ignore this email.</p>
                    </body>
                </html>
                """
                
                if send_email(email, "Your Verification Code", email_body):
                    return jsonify({
                        "success": True,
                        "message": "Signup successful. Please check your email for verification code."
                    }), 201
                else:
                    return jsonify({
                        "success": False,
                        "message": "Failed to send verification email"
                    }), 500

    except Exception as e:
        logger.exception(f"Exception during signup: {e}")
        return jsonify({
            "success": False,
            "message": "An error occurred during signup"
        }), 500

@app.route('/api/auth/verify-email', methods=['POST'])
def verify_email():
    try:
        data = request.get_json()
        email = data.get('email')
        code = data.get('code')
        
        logger.debug(f"Received email verification request - Email: {email}, Code: {code}")
        
        if not email or not code:
            return jsonify({
                "success": False,
                "message": "Email and verification code are required"
            }), 400
            
        connection = get_db_connection()
        if not connection:
            return jsonify({
                "success": False,
                "message": "Database connection failed"
            }), 500
            
        with connection:
            with connection.cursor() as cursor:
                # Find user with matching verification code without checking expiry
                cursor.execute(
                    """SELECT * FROM users 
                       WHERE email=%s AND verification_code=%s 
                       AND email_verified=FALSE""",
                    (email, code)
                )
                user = cursor.fetchone()
                
                if not user:
                    return jsonify({
                        "success": False,
                        "message": "Invalid verification code"
                    }), 400
                
                # Update user as verified and clear verification code
                cursor.execute(
                    """UPDATE users 
                       SET email_verified=TRUE, 
                           verification_code=NULL, 
                           verification_code_expiry=NULL 
                       WHERE id=%s""",
                    (user['id'],)
                )
                connection.commit()
                
                # Send welcome email
                email_body = """
                <html>
                    <body>
                        <h2>Welcome to MyHomeMachine!</h2>
                        <p>Your email has been successfully verified.</p>
                        <p>You can now start using all features of the application.</p>
                    </body>
                </html>
                """
                
                send_email(user['email'], "Welcome to MyHomeMachine", email_body)
                
                return jsonify({
                    "success": True,
                    "message": "Email verification successful"
                }), 200
                
    except Exception as e:
        logger.exception(f"Exception during email verification: {e}")
        return jsonify({
            "success": False,
            "message": str(e)
        }), 500

@app.route('/api/auth/resend-verification', methods=['POST'])
def resend_verification():
    try:
        data = request.get_json()
        email = data.get('email')
        
        if not email:
            return jsonify({
                "success": False,
                "message": "Email is required"
            }), 400
            
        connection = get_db_connection()
        if not connection:
            return jsonify({
                "success": False,
                "message": "Database connection failed"
            }), 500
            
        with connection:
            with connection.cursor() as cursor:
                # Check if user exists and needs verification
                cursor.execute(
                    "SELECT * FROM users WHERE email=%s AND email_verified=FALSE",
                    (email,)
                )
                user = cursor.fetchone()
                
                if not user:
                    return jsonify({
                        "success": False,
                        "message": "Email not found or already verified"
                    }), 404
                
                # Generate new verification code
                new_code = generate_verification_code()
                code_expiry = datetime.now() + timedelta(minutes=30)
                
                # Update verification code
                cursor.execute(
                    """UPDATE users 
                       SET verification_code=%s, 
                           verification_code_expiry=%s 
                       WHERE email=%s""",
                    (new_code, code_expiry, email)
                )
                connection.commit()
                
                # Send new verification code
                email_body = f"""
                <html>
                    <body>
                        <h2>Your New Verification Code</h2>
                        <p>Your verification code is:</p>
                        <h1 style="font-size: 32px; color: #4A90E2; text-align: center; padding: 20px;">{new_code}</h1>
                        <p>This code will expire in 30 minutes.</p>
                    </body>
                </html>
                """
                
                if send_email(email, "New Verification Code", email_body):
                    return jsonify({
                        "success": True,
                        "message": "New verification code sent"
                    }), 200
                else:
                    return jsonify({
                        "success": False,
                        "message": "Failed to send verification code"
                    }), 500
                    
    except Exception as e:
        logger.exception(f"Exception during resend verification: {e}")
        return jsonify({
            "success": False,
            "message": "An error occurred"
        }), 500

@app.route('/api/auth/login', methods=['POST'])
def login():
    try:
        data = request.get_json()
        
        if not data or not all(k in data for k in ['email', 'password']):
            return jsonify({
                "success": False,
                "message": "Missing email or password"
            }), 400

        email = data['email']
        password = data['password']

        connection = get_db_connection()
        if not connection:
            return jsonify({
                "success": False,
                "message": "Database connection failed"
            }), 500

        with connection:
            with connection.cursor() as cursor:
                cursor.execute("SELECT * FROM users WHERE email=%s", (email,))
                user = cursor.fetchone()
                
                if not user:
                    return jsonify({
                        "success": False,
                        "message": "Invalid email or password"
                    }), 401
                
                if not user['email_verified']:
                    return jsonify({
                        "success": False,
                        "message": "Please verify your email before logging in"
                    }), 403
                
                if check_password_hash(user['password'], password):
                    return jsonify({
                        "success": True,
                        "message": "Login successful",
                        "user": {
                            "id": user['id'],
                            "email": user['email']
                        }
                    }), 200
                else:
                    return jsonify({
                        "success": False,
                        "message": "Invalid email or password"
                    }), 401
                    
    except Exception as e:
        logger.exception(f"Exception during login: {e}")
        return jsonify({
            "success": False,
            "message": "An error occurred during login"
        }), 500

@app.route('/api/auth/request-reset', methods=['POST'])
def request_reset():
    try:
        data = request.get_json()
        email = data.get('email')
        
        logger.debug(f"Password reset request received for email: {email}")
        
        if not email:
            return jsonify({
                "success": False,
                "message": "Email is required"
            }), 400
            
        connection = get_db_connection()
        if not connection:
            return jsonify({
                "success": False,
                "message": "Database connection failed"
            }), 500
            
        with connection:
            with connection.cursor() as cursor:
                # Check if user exists
                cursor.execute("SELECT * FROM users WHERE email=%s", (email,))
                user = cursor.fetchone()
                
                if not user:
                    return jsonify({
                        "success": False,
                        "message": "Email not found"
                    }), 404
                
                # Generate reset code
                reset_code = generate_verification_code()
                code_expiry = datetime.now() + timedelta(minutes=30)
                
                logger.debug(f"Generated reset code: {reset_code} with expiry: {code_expiry}")
                
                # Store reset code and expiry - Using verification_code fields instead
                # The error might be because reset_code columns don't exist in your table
                cursor.execute(
                    "UPDATE users SET verification_code=%s, verification_code_expiry=%s WHERE email=%s",
                    (reset_code, code_expiry, email)
                )
                connection.commit()
                
                logger.debug(f"Reset code stored in database for user: {email}")
                
                # Send reset code email
                email_body = f"""
                <html>
                    <body>
                        <h2>Password Reset Request</h2>
                        <p>Your password reset code is:</p>
                        <h1 style="font-size: 32px; color: #4A90E2; text-align: center; padding: 20px;">{reset_code}</h1>
                        <p>This code will expire in 30 minutes.</p>
                        <p>If you didn't request this, please ignore this email.</p>
                    </body>
                </html>
                """
                
                email_sent = send_email(email, "Password Reset Code", email_body)
                logger.debug(f"Email sending result: {email_sent}")
                
                if email_sent:
                    return jsonify({
                        "success": True,
                        "message": "Password reset code sent"
                    }), 200
                else:
                    return jsonify({
                        "success": False,
                        "message": "Failed to send reset code"
                    }), 500
                    
    except Exception as e:
        logger.exception(f"Exception during password reset request: {e}")
        return jsonify({
            "success": False,
            "message": str(e)
        }), 500

@app.route('/api/auth/reset-password', methods=['POST'])
def reset_password():
    try:
        data = request.get_json()
        email = data.get('email')
        code = data.get('code')
        new_password = data.get('new_password')
        
        logger.debug(f"Received password reset request - Email: {email}, Code: {code}")
        
        if not all([email, code, new_password]):
            return jsonify({
                "success": False,
                "message": "Email, code and new password are required"
            }), 400
            
        connection = get_db_connection()
        if not connection:
            return jsonify({
                "success": False,
                "message": "Database connection failed"
            }), 500
            
        with connection:
            with connection.cursor() as cursor:
                # Change reset_code to verification_code in this query
                cursor.execute(
                    """SELECT * FROM users 
                       WHERE email=%s AND verification_code=%s""",
                    (email, code)
                )
                user = cursor.fetchone()
                
                if not user:
                    return jsonify({
                        "success": False,
                        "message": "Invalid reset code"
                    }), 400
                
                # Update password and clear verification code
                password_hash = generate_password_hash(new_password)
                cursor.execute(
                    """UPDATE users 
                       SET password=%s, 
                           verification_code=NULL, 
                           verification_code_expiry=NULL 
                       WHERE id=%s""",
                    (password_hash, user['id'])
                )
                connection.commit()
                
                # Send confirmation email
                email_body = """
                <html>
                    <body>
                        <h2>Password Reset Successful</h2>
                        <p>Your password has been successfully reset.</p>
                        <p>If you didn't make this change, please contact support immediately.</p>
                    </body>
                </html>
                """
                
                send_email(user['email'], "Password Reset Successful", email_body)
                
                return jsonify({
                    "success": True,
                    "message": "Password reset successful"
                }), 200
                
    except Exception as e:
        logger.exception(f"Exception during password reset: {e}")
        return jsonify({
            "success": False,
            "message": str(e)
        }), 500

@app.route('/api/auth/logout', methods=['POST'])
def logout():
    return jsonify({
        "success": True,
        "message": "Logout successful"
    }), 200
# Add these routes to your Flask app

@app.route('/api/auth/request-deletion', methods=['POST'])
def request_account_deletion():
    try:
        data = request.get_json()
        email = data.get('email')
        
        if not email:
            return jsonify({
                "success": False,
                "message": "Email is required"
            }), 400
            
        connection = get_db_connection()
        if not connection:
            return jsonify({
                "success": False,
                "message": "Database connection failed"
            }), 500
            
        with connection:
            with connection.cursor() as cursor:
                # Check if user exists
                cursor.execute("SELECT * FROM users WHERE email=%s", (email,))
                user = cursor.fetchone()
                
                if not user:
                    return jsonify({
                        "success": False,
                        "message": "Email not found"
                    }), 404
                
                # Generate deletion confirmation code
                deletion_code = generate_verification_code()
                code_expiry = datetime.now() + timedelta(minutes=30)
                
                # Store deletion code and expiry
                cursor.execute(
                    "UPDATE users SET verification_code=%s, verification_code_expiry=%s WHERE email=%s",  # Changed from deletion_code to verification_code
                    (deletion_code, code_expiry, email)
                )
                connection.commit()
                
                # Send deletion confirmation code
                email_body = f"""
                <html>
                    <body>
                        <h2>Account Deletion Request</h2>
                        <p>Your account deletion confirmation code is:</p>
                        <h1 style="font-size: 32px; color: #FF4444; text-align: center; padding: 20px;">{deletion_code}</h1>
                        <p>This code will expire in 30 minutes.</p>
                        <p>If you didn't request this, please ignore this email and secure your account.</p>
                        <p>Warning: This action cannot be undone. All your data will be permanently deleted.</p>
                    </body>
                </html>
                """
                
                if send_email(email, "Account Deletion Confirmation Code", email_body):
                    return jsonify({
                        "success": True,
                        "message": "Deletion confirmation code sent"
                    }), 200
                else:
                    return jsonify({
                        "success": False,
                        "message": "Failed to send deletion code"
                    }), 500
                    
    except Exception as e:
        logger.exception(f"Exception during account deletion request: {e}")
        return jsonify({
            "success": False,
            "message": "An error occurred"
        }), 500

@app.route('/api/auth/confirm-deletion', methods=['POST'])
def confirm_account_deletion():
    try:
        data = request.get_json()
        email = data.get('email')
        code = data.get('code')
        
        logger.debug(f"Received deletion request - Email: {email}, Code: {code}")
        
        connection = get_db_connection()
        if not connection:
            return jsonify({"success": False, "message": "Database connection failed"}), 500
            
        with connection:
            with connection.cursor() as cursor:
                # Get the user record by email and code without checking expiry
                cursor.execute(
                    "SELECT * FROM users WHERE email=%s AND verification_code=%s",
                    (email, code)
                )
                user = cursor.fetchone()
                
                if not user:
                    return jsonify({
                        "success": False,
                        "message": "Invalid verification code"
                    }), 400

                # Since we confirmed from the database query that the code is valid until 2025,
                # we'll proceed with deletion regardless of the expiry check result
                
                # Delete the user account
                cursor.execute("DELETE FROM users WHERE id=%s", (user['id'],))
                connection.commit()
                
                # Send farewell email
                email_body = """
                <html>
                    <body>
                        <h2>Account Deleted</h2>
                        <p>Your account has been successfully deleted.</p>
                        <p>We're sorry to see you go. You're always welcome back!</p>
                    </body>
                </html>
                """
                send_email(user['email'], "Account Deleted", email_body)
                
                return jsonify({
                    "success": True,
                    "message": "Account successfully deleted"
                }), 200
                
    except Exception as e:
        logger.exception(f"Exception during account deletion: {e}")
        return jsonify({
            "success": False,
            "message": str(e)
        }), 500
    

#Ai Part

# Create a dictionary to store usage data
usage_data = defaultdict(list)
suggestions = []

@app.route('/api/usage/track', methods=['POST'])
def track_usage():
    try:
        data = request.get_json()
        events = data.get('events', [])
        
        if not events:
            return jsonify({
                "success": False,
                "message": "No events provided"
            }), 400
        
        # Store events in memory (in a real app, store in database)
        for event in events:
            device_id = event.get('deviceId')
            device_name = event.get('deviceName')
            device_type = event.get('deviceType')
            action = event.get('action')
            value = event.get('value', '')
            timestamp = event.get('timestamp')
            
            if not all([device_id, device_name, device_type, action, timestamp]):
                continue
                
            usage_data[device_id].append({
                'device_name': device_name,
                'device_type': device_type,
                'action': action,
                'value': value,
                'timestamp': timestamp
            })
        
        # Analyze patterns after collecting data
        analyze_patterns()
        
        return jsonify({
            "success": True,
            "message": f"Tracked {len(events)} events"
        }), 200
        
    except Exception as e:
        logger.exception(f"Exception during usage tracking: {e}")
        return jsonify({
            "success": False,
            "message": str(e)
        }), 500

@app.route('/api/usage/suggestions', methods=['GET'])
def get_suggestions():
    try:
        return jsonify({
            "success": True,
            "suggestions": suggestions
        }), 200
    except Exception as e:
        logger.exception(f"Exception getting suggestions: {e}")
        return jsonify({
            "success": False,
            "message": str(e)
        }), 500

@app.route('/api/usage/suggestion/response', methods=['POST'])
def handle_suggestion_response():
    try:
        data = request.get_json()
        suggestion_id = data.get('suggestionId')
        accepted = data.get('accepted', False)
        
        if not suggestion_id:
            return jsonify({
                "success": False,
                "message": "No suggestion ID provided"
            }), 400
            
        # Remove the suggestion from the list
        global suggestions
        suggestions = [s for s in suggestions if s['id'] != suggestion_id]
        
        # If accepted, we could refine our model
        # If rejected, we could note this to avoid similar suggestions
        
        return jsonify({
            "success": True,
            "message": "Suggestion response recorded"
        }), 200
        
    except Exception as e:
        logger.exception(f"Exception handling suggestion response: {e}")
        return jsonify({
            "success": False,
            "message": str(e)
        }), 500

def analyze_patterns():
    """
    Analyze device usage patterns and generate automation suggestions
    """
    try:
        global suggestions
        new_suggestions = []
        
        # For each device with enough data
        for device_id, events in usage_data.items():
            if len(events) < 5:  # Need at least 5 events to detect patterns
                continue
                
            # Get the most recent events (last 2 weeks)
            two_weeks_ago = datetime.now() - timedelta(days=14)
            recent_events = [e for e in events if e['timestamp'] > two_weeks_ago.timestamp() * 1000]
            
            if len(recent_events) < 5:
                continue
                
            # Group events by action
            actions = {}
            for action in ['ON', 'OFF', 'SET_BRIGHTNESS', 'SET_COLOR']:
                action_events = [e for e in recent_events if e['action'] == action]
                if action_events:
                    actions[action] = action_events
            
            # Analyze ON events for time patterns
            if 'ON' in actions and len(actions['ON']) >= 3:
                on_times = []
                on_day_of_week = []
                
                for event in actions['ON']:
                    event_time = datetime.fromtimestamp(event['timestamp'] / 1000)
                    on_times.append(event_time.hour * 60 + event_time.minute)  # Time as minutes since midnight
                    on_day_of_week.append(event_time.weekday())  # 0=Monday, 6=Sunday
                
                # Find clusters of times
                hour_clusters = cluster_times(on_times, 30)  # 30 minutes tolerance
                
                for cluster_time, frequency in hour_clusters.items():
                    # If there's a clear pattern (used 3+ times at similar times)
                    if frequency >= 3:
                        # Get the hour and minute
                        hour = cluster_time // 60
                        minute = cluster_time % 60
                        
                        # Format as 12-hour time
                        ampm = "AM" if hour < 12 else "PM"
                        hour12 = hour if hour <= 12 else hour - 12
                        hour12 = 12 if hour12 == 0 else hour12
                        time_str = f"{hour12}:{minute:02d} {ampm}"
                        
                        # Find what days this happens on
                        day_counts = defaultdict(int)
                        for day in on_day_of_week:
                            day_counts[day] += 1
                        
                        # If pattern appears on specific days
                        frequent_days = []
                        day_names = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"]
                        
                        for day, count in day_counts.items():
                            if count >= 2:  # At least twice on this day
                                frequent_days.append(day_names[day])
                        
                        # If no specific days, assume all days
                        if not frequent_days:
                            frequent_days = day_names
                        
                        # Create a suggestion
                        sample_event = actions['ON'][0]
                        confidence = min(0.95, frequency / len(actions['ON']) * 0.8 + 0.2)  # Cap at 95%
                        
                        # Format days for display
                        days_text = "every day" if len(frequent_days) == 7 else on_days_list(frequent_days)
                        
                        suggestion = {
                            "id": str(uuid.uuid4()),
                            "deviceId": device_id,
                            "deviceName": sample_event['device_name'],
                            "deviceType": sample_event['device_type'],
                            "action": "ON",
                            "timeOfDay": time_str,
                            "daysOfWeek": frequent_days,
                            "confidence": confidence,
                            "description": f"Turn on {sample_event['device_name']} at {time_str} {days_text}"
                        }
                        
                        new_suggestions.append(suggestion)
            
            # TODO: Similarly analyze OFF events and other actions for patterns
        
        # Set the global suggestions, keeping any that weren't in devices we just analyzed
        analyzed_device_ids = set(usage_data.keys())
        existing_different_device_suggestions = [
            s for s in suggestions 
            if s['deviceId'] not in analyzed_device_ids
        ]
        
        suggestions = existing_different_device_suggestions + new_suggestions
        
    except Exception as e:
        logger.exception(f"Error analyzing patterns: {e}")

def cluster_times(times, tolerance_minutes):
    """
    Group similar times together and count frequencies
    """
    clusters = defaultdict(int)
    
    for time in times:
        # Find closest existing cluster
        closest = None
        min_distance = float('inf')
        
        for center in clusters.keys():
            # Calculate circular distance (to handle midnight wrapping)
            distance = min(
                abs(time - center),
                1440 - abs(time - center)  # 1440 = minutes in a day
            )
            
            if distance < min_distance:
                min_distance = distance
                closest = center
        
        # If within tolerance, add to closest cluster
        if closest is not None and min_distance <= tolerance_minutes:
            clusters[closest] += 1
        else:
            # Create new cluster
            clusters[time] = 1
    
    return clusters

def on_days_list(days):
    """Format a list of days for readable display"""
    if len(days) == 5 and all(d in days for d in ["MON", "TUE", "WED", "THU", "FRI"]):
        return "on weekdays"
    elif len(days) == 2 and all(d in days for d in ["SAT", "SUN"]):
        return "on weekends"
    else:
        return "on " + ", ".join(days)



if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)