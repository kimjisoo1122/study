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

function validWriter() {
  const writerElm = document.querySelector('.writer-input');
  const writerLength = writerElm.value.length;
  const errElm = document.querySelector('.writer-input-error');

  if (writerLength < 3 || writerLength >= 5) {
    writerElm.classList.add('input-error');
    errElm.style.display = 'block';
    errElm.textContent = '3글자 이상, 5글자 미만 이여야 합니다.';
    return false;
  }

  writerElm.classList.remove('input-error');
  errElm.style.display = 'none';

  return true;
}

function validPassword() {
  const pwdElm = document.querySelector('.password-input');
  const confirmElm = document.querySelector('.password-input-confirm');
  const pwd = pwdElm.value;
  const confirmPwd = confirmElm.value;
  const errElm = document.querySelector('.password-input-error');

  if (pwd.length < 4 || pwd.length > 15) {
    pwdElm.classList.add('input-error');
    errElm.style.display = 'block';
    errElm.textContent = '4글자 이상, 16글자 미만 이여야 합니다.';

    return false;
  }

  const pwdRex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&+=!]).*$/;
  if (!pwdRex.test(pwd)) {
    pwdElm.classList.add('input-error');
    errElm.style.display = 'block';
    errElm.textContent = '영문/숫자/특수문자를 포함 하여야 합니다.';

    return false;
  }

  if (pwd.length !== confirmPwd.length) {
    pwdElm.classList.add('input-error');
    errElm.style.display = 'block';
    errElm.textContent = '비밀번호가 서로 다릅니다.';

    return false;
  }

  pwdElm.classList.remove('input-error');
  errElm.style.display = 'none';
}

function validTitle() {
  const titleElm = document.querySelector('.title-input');
  const errElm = document.querySelector('.title-input-error');

  if (titleElm.value.length < 4 || titleElm.value.length > 99) {
    titleElm.classList.add('input-error')
    errElm.style.display = 'block';
    errElm.textContent = '4글자 이상, 100글자 미만 이여야 합니다.';

    return false;
  }

  titleElm.classList.remove('input-error');
  errElm.style.display = 'none';

  return true;
}

function validContent() {
  const contentElm = document.querySelector('.content-text');
  const errElm = document.querySelector('.content-text-error');

  if (contentElm.value.length < 4 || contentElm.value.length > 1999) {
    contentElm.classList.add('input-error');
    errElm.style.display = 'block'
    errElm.textContent = '4글자 이상, 2000글자 미만 이여야 합니다';

    return false;
  }

  contentElm.classList.remove('input-error');
  errElm.style.display = 'none';

  return true;
}

function validForm() {
  const validatedWriter = validWriter();
  const validatePassword = validPassword();
  const validateTitle = validTitle();
  const validateContent = validContent();

  return validatedWriter && validatePassword && validateTitle && validateContent;
}
