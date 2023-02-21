// let tabBtns = document.querySelectorAll('.tab-button');
// for (let i = 0; i < tabBtns.length; i++) {
// 	tabBtns[i].addEventListener('click', () => {
// 		let children = document.querySelector('.list').children;
// 		for (const child of children) {
// 			child.classList.remove('orange')
// 		}
// 		tabBtns[i].classList.add('orange');
//
// 		let tabContents = document.querySelectorAll('.tab-content');
// 		for (const tabContent of tabContents) {
// 			tabContent.classList.remove('show');
// 		}
// 		tabContents[i].classList.add('show');
// 	})
// }
let tabBtns = $('.tab-button');
let tabContents = $('.tab-content');

function tapOpen(i) {
	tabBtns.removeClass('orange');
	tabBtns.eq(i).addClass('orange');
	tabContents.removeClass('show');
	tabContents.eq(i).addClass('show');
}

// for (let i = 0; i < tabBtns.length; i++) {
// 	tabBtns.eq(i).on('click', () => {
// 		tapOpen(i);
// 	})
// }

$('.list').click(function (e) {
	tapOpen(parseInt(e.target.dataset.id));
})
