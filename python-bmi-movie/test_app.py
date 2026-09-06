import pytest
from app import app, calculate_bmi

def test_bmi_calculation():
    bmi, cat = calculate_bmi(70, 1.75)
    assert bmi == 22.86
    assert cat == "Normal weight"

def test_invalid_bmi():
    bmi, cat = calculate_bmi(-50, 1.75)
    assert bmi is None
    assert cat == "Invalid input"

def test_flask_route():
    client = app.test_client()
    response = client.get('/')
    assert response.status_code == 200
    assert b"BMI Calculator" in response.data