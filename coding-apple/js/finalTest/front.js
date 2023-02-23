document.querySelector('.nav-list').addEventListener('click', function (e) {
	document.querySelectorAll('.nav-list-item').forEach(i => {
		i.classList.remove('list-on');
	})
	e.target.classList.add('list-on');
});

	fetch('./store.json')
			.then(res => res.json())
			.then(data => {
				data.products.forEach((item, i) => {
					document.querySelector('.main-card-container').insertAdjacentHTML('beforeend',
							`<div class="main-card-outer" id="data${i}" draggable="true" ondragstart="drag(event)" >
		        <div class="main-card-inner">
		          <div class="main-card-img">
		            <img src=${item.photo} alt="카드사진" draggable="false">
		          </div>
		          <h4 class="main-card-title">${item.title}</h4>
		          <p class="main-card-sub">${item.brand}</p>
		          <p class="main-card-price">${item.price}</p>
		          <button class="main-card-btn">담기</button>
		        </div>
		      </div>`
					)
				});
			});


document.querySelector('.search-input').addEventListener('change', function (e) {
	let inputText = this.value;

	fetch('./store.json')
			.then(res => res.json())
			.then(data => {
				document.querySelectorAll('.main-card-outer').forEach(e => e.remove());
				let filteredData = data.products.filter(e => e.title.includes(inputText));
				filteredData.forEach(item => {
							let replaceText = item.title.replace(inputText, `<span class="search-text">${inputText}</span>`);
					document.querySelector('.main-card-container').insertAdjacentHTML('beforeend',
							`<div class="main-card-outer"  draggable="true">
		        <div class="main-card-inner">
		          <div class="main-card-img">
		            <img src=${item.photo} alt="카드사진" draggable="false">
		          </div>
		          <h4 class="main-card-title">${replaceText}</h4>
		          <p class="main-card-sub">${item.brand}</p>
		          <p class="main-card-price">${item.price}</p>
		          <button class="main-card-btn">담기</button>
		        </div>
		      </div>`
					)
				})
			})
});

function drag(e) {
	console.log(e.target.id);
	e.dataTransfer.setData('data', e.target.id);
}

document.querySelector('.cart-drag-container').addEventListener('dragover', function(e) {
	e.preventDefault();
})


function totalPrice(e) {
	let quantity = e.target.value;
	let price = e.target.previousElementSibling.innerHTML;
	let totalPrice = price * quantity;
	console.log(totalPrice);
}

document.querySelector('.cart-drag-container').addEventListener('drop', function (e) {
	let data = e.dataTransfer.getData('data');
	if (data) {
		let item = document.querySelector(`#${data}`);
		item.style.cursor = 'cursor';
		let card_inner = item.children[0];
		card_inner.style.textAlign = 'center';
		card_inner.style.borderRadius = '5px';
		card_inner.children[card_inner.childElementCount - 1].remove();
		card_inner.insertAdjacentHTML('beforeend',
				`<input type="text" class="cart-quantity" oninput="totalPrice(event)">`)
		e.target.appendChild(item);
		document.querySelector('.cart-drag-text').innerHTML = '';
	}
});

