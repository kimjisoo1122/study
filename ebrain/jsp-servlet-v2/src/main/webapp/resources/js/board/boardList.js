// 제목이 80글자 넘어가면 ... 처리
window.onload = () => {
  const titleElms = document.querySelectorAll('.list-title-a');
  for (const titleElm of titleElms) {
    const innerText = titleElm.innerText;
    if (innerText.length > 80) {
      titleElm.innerText = innerText.substring(0, 80) + '...';
    }
  }

  // 검색 폼 전송 유효성 검증
  const formElm = document.querySelector('.search-form');
  formElm.addEventListener('submit', (e) => {
    e.preventDefault();
    const searchElm = document.querySelector('.condition-search');
    const fromDateElm = document.querySelector('.date-from');
    const toDateElm = document.querySelector('.date-to');

    if (fromDateElm.value !== '') {
      if (toDateElm.value === '') {
        toDateElm.style.border = '1px solid red';
        return false;
      } else {
        toDateElm.style.border = '1px solid black';
      }
    }

    if (toDateElm.value !== '') {
      if (fromDateElm.value === '') {
        fromDateElm.style.border = '1px solid red';
        return  false;
      } else {
        fromDateElm.style.border = '1px solid black';
      }
    }

    formElm.submit();
  })
}

