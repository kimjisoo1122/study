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

  if (confirmElm) {
    if (pwd.length !== confirmElm.value.length) {
      pwdElm.classList.add('input-error');
      errElm.style.display = 'block';
      errElm.textContent = '비밀번호가 서로 다릅니다.';

      return false;
    }
  }

  pwdElm.classList.remove('input-error');
  errElm.style.display = 'none';

  return true;
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
  const validatedPassword = validPassword();
  const validatedTitle = validTitle();
  const validatedContent = validContent();
  console.log(validatedWriter && validatedPassword && validatedTitle && validatedContent);

  return validatedWriter && validatedPassword && validatedTitle && validatedContent;
}