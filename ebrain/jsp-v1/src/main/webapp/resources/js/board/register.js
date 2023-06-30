function uploadFile(thisElement) {
  const fileName = thisElement.value.split('/').pop().split('\\').pop();
  const disabledElm = thisElement.parentElement.querySelector('input[disabled]');
  disabledElm.value = fileName;

  // 파일 사이즈 체크
  if (thisElement.files) {
    const fileSize = thisElement.files[0].size;

    if (fileSize / (1024 * 1024) > 10) {
      thisElement.value = '';
      disabledElm.value = '10MB를 넘을 수 없습니다.';
      disabledElm.style.color = 'red';
    } else {
      disabledElm.style.color = 'black';
    }
  }
}
