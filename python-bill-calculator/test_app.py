from app import calculate_total_bill

def test_bill_calculation():
    total, breakdown = calculate_total_bill(100, 2, 5)
    assert total == 210.0
    assert "Subtotal: $200.00" in breakdown

def test_invalid_input():
    total, msg = calculate_total_bill(-10, 2)
    assert total is None