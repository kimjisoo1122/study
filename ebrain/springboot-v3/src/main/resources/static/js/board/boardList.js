/**
 * Html태그가 모드 로딩되면 제목이 80글자 넘어가면 이후 글자를 ... 처리합니다.
 */
window.onload = () => {
  const titleElms = document.querySelectorAll('.list-title-a');
  for (const titleElm of titleElms) {
    const innerText = titleElm.innerText;
    if (innerText.length > 80) {
      titleElm.innerText = innerText.substring(0, 80) + '...';
    }
  }
}

/**
 * 게시글 목록의 검색조건을 전송할때 유효성검증을 합니다.
 */
function validCondition() {
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

  return true;
}

