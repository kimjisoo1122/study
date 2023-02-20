document.querySelector('.tab-button')
		.addEventListener('click', () => {
			document.querySelector('.tab-button').classList = 'tab-button orange';
			document.querySelectorAll('.tab-button')[1].classList = 'tab-button';
			
		})