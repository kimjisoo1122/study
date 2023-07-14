/**
 * 게시글의 수정과 등록에 쓰이는 게시글의 유효성검증 스크립트 입니다.
 */


/**
 * 카테고리를 검증합니다.
 */
function validCategory() {
  const categoryElm = document.querySelector('.category-select');
  const categoryLength = categoryElm.value.length;
  const errElm = document.querySelector('.category-select-error');
  const thErrElm = document.querySelector('.category-select-error.th-err');

  if (thErrElm) {
    thErrElm.style.display = 'none';
  }

  if (categoryLength === 0) {
    categoryElm.classList.add('select-error');
    errElm.style.display = 'block';
    errElm.textContent = '카테고리는 필수 항목 입니다.';
    return false;
  }

  categoryElm.classList.remove('select-error');
  errElm.style.display = 'none';

  return true;
}

/**
 * 작성자를 검증합니다.
 */

function validWriter() {
  const writerElm = document.querySelector('.writer-input');
  const writerLength = writerElm.value.length;
  const errElm = document.querySelector('.writer-input-error');
  const thErrElm = document.querySelector('.writer-input-error.th-err');

  if (thErrElm) {
    thErrElm.style.display = 'none';
  }

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

/**
 * 비밀번호를 검증합니다.
 */
function validPassword() {
  const pwdElm = document.querySelector('.password-input');
  const confirmElm = document.querySelector('.password-input-confirm');
  const pwd = pwdElm.value;
  const errElm = document.querySelector('.password-input-error');
  const thErrElm = document.querySelector('.password-input-error.th-err');

  if (thErrElm) {
    thErrElm.style.display = 'none';
  }

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

  // 등록폼의 경우에만 진행합니다.
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

/**
 * 제목을 검증합니다.
 */
function validTitle() {
  const titleElm = document.querySelector('.title-input');
  const errElm = document.querySelector('.title-input-error');
  const thErrElm = document.querySelector('.title-input-error.th-err');

  if (thErrElm) {
    thErrElm.style.display = 'none';
  }

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

/**
 * 내용을 검증합니다.
 */
function validContent() {
  const contentElm = document.querySelector('.content-text');
  const errElm = document.querySelector('.content-text-error');
  const thErrElm = document.querySelector('.content-text-error.th-err');

  if (thErrElm) {
    thErrElm.style.display = 'none';
  }

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

/**
 * 폼 전송시 모든 유효성검증을 진행합니다.
 */
function validForm() {
  const validatedWriter = validWriter();
  const validatedPassword = validPassword();
  const validatedTitle = validTitle();
  const validatedContent = validContent();
  const validatedCategory = validCategory();

  const validateResult = validatedWriter && validatedPassword
      && validatedTitle && validatedContent && validatedCategory;

  if (!validateResult) {
    console.error('유효성 검증 실패');
  }

  return validateResult;
}