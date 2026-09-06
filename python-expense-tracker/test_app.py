import pytest
from app import app, expenses_db, add_expense, get_total_expenses

def test_add_valid_expense():
    initial_count = len(expenses_db)
    result = add_expense("Books", 25.00, "Education")
    assert result is True
    assert len(expenses_db) == initial_count + 1

def test_add_invalid_amount():
    result = add_expense("Lunch", -10, "Food")
    assert result is False

def test_total_calculation():
    current_total = get_total_expenses()
    add_expense("Snacks", 10.00, "Food")
    assert get_total_expenses() == current_total + 10.00

def test_flask_route():
    client = app.test_client()
    response = client.get('/')
    assert response.status_code == 200
    assert b"Online Expense Tracker" in response.data