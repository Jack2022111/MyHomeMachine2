# MyHomeMachine Backend

## Project Structure YOU NEED ".env"
```
myhomemachine-backend/
├── app.py
├── requirements.txt
└── .env
```

## Setup and Installation

### Virtual Environment
The virtual environment has already been created. You just need to activate it.
If not, delete venv file and run this command:
```
 python -m venv venv
```
#### On Mac:
```
source venv/bin/activate
```

#### On Windows:

**Command Prompt:**
```
venv\Scripts\activate
```

**PowerShell:**
```
.\venv\Scripts\Activate.ps1
```

If you encounter permission issues in PowerShell, run PowerShell as Administrator and execute one of these commands:

**Option 1** - More permissive, allows all local scripts to run:
```
Set-ExecutionPolicy RemoteSigned
```

**Option 2** - More restrictive, only allows scripts in the current session:
```
Set-ExecutionPolicy Bypass -Scope Process
```

### Installing Dependencies
If needed, install the required packages:
```
pip install -r requirements.txt
```

## Testing

The API can be tested using Postman:

1. Make a GET request to: `http://127.0.0.1:5000/api/health`
2. This endpoint checks login and username status
3. Expected response:
```json
{
    "status": "healthy"
}
```
