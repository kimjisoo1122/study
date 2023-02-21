// function pay(price, firstOrder) {
// 	return firstOrder ? price * 0.9 - 1.5 : price * 0.9;
// }
//
//
// let pay1 = pay(10, true);
// console.log(parseFloat(pay1.toFixed(2)));
var 출석부 = ['흥민', '영희', '철수', '재석'];
let string = 이름찾기('흥민1');
console.log(string)
function 이름찾기(name){
	return 출석부.filter(s => name === s).length >= 1 ? '있어요' : '';
}


function 구구단() {
	for (let i = 2; i <= 9; i++) {
		for (let k = 1; k <= 9; k++) {
			console.log(i * k);
		}
	}
}

// 구구단();


함수([40,40,40], 60);

function 함수(arr, num) {
	let sum = 0;
	arr.forEach(i => {
		sum += i;
	})
	let average = sum / Math.round(arr.length);
	let result = num - average;
	if (result > 0) {
		console.log(`평균보다 ${result}점이 올랐네요`);
	} else {
		console.log(`평균보다 ${-result}점이 떨어졌네요 재수추천`);
	}
}