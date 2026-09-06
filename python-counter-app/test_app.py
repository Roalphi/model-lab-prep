from app import update_counter

def test_counter_logic():
    assert update_counter(5, 'inc') == 6
    assert update_counter(5, 'dec') == 4
    assert update_counter(5, 'reset') == 0