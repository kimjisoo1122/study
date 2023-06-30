function uploadFile(thisElm) {
  document.querySelector('.file-error').remove();

  const fileName = thisElm.value.split('/').pop().split('\\').pop();
  const disabledElm = thisElm.parentElement.querySelector('input[disabled]');
  disabledElm.value = fileName;

  if (thisElm.files) {
    const fileSize = thisElm.files[0].size;

    if (fileSize / (1024 * 1024) > 10) {
      thisElm.value = '';
      disabledElm.value = '10MB를 넘을 수 없습니다.';
      disabledElm.style.color = 'red';
    } else {
      disabledElm.style.color = 'black';
    }
  }
}
