import pytest
from app import app, calculate

def test_calculator_logic():
    assert calculate(10, 5, 'add') == 15
    assert calculate(10, 5, 'subtract') == 5
    assert calculate(10, 5, 'multiply') == 50
    assert calculate(10, 5, 'divide') == 2.0
    assert calculate(10, 0, 'divide') == "Error: Division by zero"

def test_flask_route():
    client = app.test_client()
    response = client.get('/')
    assert response.status_code == 200