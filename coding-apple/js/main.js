function pay(price, firstOrder) {
	return firstOrder ? price * 0.9 - 1.5 : price * 0.9;
}


let pay1 = pay(10, true);
console.log(parseFloat(pay1.toFixed(2)));
