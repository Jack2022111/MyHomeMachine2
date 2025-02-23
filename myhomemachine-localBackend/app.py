from flask import Flask, request, jsonify
from flask_cors import CORS
import pymysql
from werkzeug.security import generate_password_hash, check_password_hash
from dotenv import load_dotenv
import os
import logging

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

# Configure logging
logging.basicConfig(
    level=logging.DEBUG,  # Set to DEBUG to capture all levels of logs
    format='%(asctime)s %(levelname)s %(name)s %(threadName)s : %(message)s'
)
logger = logging.getLogger(__name__)

# Create a database connection
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

# Health check endpoint
@app.route('/api/health', methods=['GET'])
def health_check():
    logger.debug("Health check requested.")
    return jsonify({"status": "healthy"}), 200

# Route for signup
@app.route('/api/auth/signup', methods=['POST'])
def signup():
    try:
        # Get JSON data
        data = request.get_json()
        logger.debug(f"Signup data received: {data}")
        
        # Validate required fields
        if not data or not all(k in data for k in ['email', 'password']):
            logger.warning("Missing required fields in signup data.")
            return jsonify({
                "success": False,
                "message": "Missing required fields"
            }), 400

        email = data['email']
        password = data['password']
        
        # Hash the password
        password_hash = generate_password_hash(password)
        logger.debug(f"Password hashed for user: {email}")

        # Insert user into the database
        connection = get_db_connection()
        if not connection:
            logger.error("Failed to establish database connection.")
            return jsonify({
                "success": False,
                "message": "Database connection failed"
            }), 500

        with connection:
            with connection.cursor() as cursor:
                # Check if email already exists
                cursor.execute("SELECT * FROM users WHERE email=%s", (email,))
                if cursor.fetchone():
                    logger.warning(f"Email already registered: {email}")
                    return jsonify({
                        "success": False,
                        "message": "Email already registered"
                    }), 409

                # Insert new user
                cursor.execute(
                    "INSERT INTO users (email, password) VALUES (%s, %s)",
                    (email, password_hash)
                )
                connection.commit()
                logger.info(f"User registered successfully: {email}")
                return jsonify({
                    "success": True,
                    "message": "Signup successful"
                }), 201
    except Exception as e:
        logger.exception(f"Exception during signup: {e}")
        return jsonify({
            "success": False,
            "message": "An error occurred during signup"
        }), 500

# Route for login
@app.route('/api/auth/login', methods=['POST'])
def login():
    try:
        # Get JSON data
        data = request.get_json()
        logger.debug(f"Login data received: {data}")
        
        # Validate required fields
        if not data or not all(k in data for k in ['email', 'password']):
            logger.warning("Missing email or password in login data.")
            return jsonify({
                "success": False,
                "message": "Missing email or password"
            }), 400

        email = data['email']
        password = data['password']

        # Fetch user from the database
        connection = get_db_connection()
        if not connection:
            logger.error("Failed to establish database connection.")
            return jsonify({
                "success": False,
                "message": "Database connection failed"
            }), 500

        with connection:
            with connection.cursor() as cursor:
                cursor.execute("SELECT * FROM users WHERE email=%s", (email,))
                user = cursor.fetchone()
                if user and check_password_hash(user['password'], password):
                    logger.info(f"User logged in successfully: {email}")
                    return jsonify({
                        "success": True,
                        "message": "Login successful",
                        "user": {
                            "id": user['id'],
                            "email": user['email']
                        }
                    }), 200
                else:
                    logger.warning(f"Invalid login attempt for email: {email}")
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


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
