'use strict'
$(function () {
	calc_price();
	$('.size').on('change', function () {
		
		calc_price();
	});

	$('.checkbox').on('change', function () {
		calc_price();
	});

	$('#noodlenum').on('change', function () {
		calc_price();
	});

	function calc_price() {
		let size = $('.size:checked').val();
		let topping_count = $('#topping input:checkbox:checked').length;
		let noodle_num = $('#noodlenum option:selected').val();
		let size_price = 0;
		let topping_price = 0;
		if (size == 'M') {
			size_price = Number($('#priceM').text());
			topping_price = 200 * topping_count;
		} else {
			size_price = Number($('#priceL').text());
			topping_price = 300 * topping_count;
		}
		let price = (size_price + topping_price ) * noodle_num;
		
		
		$('#total-price').text(price.toLocaleString());
	}
	;
});
	