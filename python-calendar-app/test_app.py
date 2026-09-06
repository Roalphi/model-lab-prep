from app import format_selected_date

def test_valid_date():
    assert format_selected_date("2026-09-06") == "September 06, 2026 (Sunday)"

def test_invalid_date():
    assert format_selected_date("bad-date") == "Invalid date format"