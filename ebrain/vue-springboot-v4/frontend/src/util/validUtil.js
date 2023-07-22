/**
 * 게시글의 수정과 등록에 쓰이는 게시글의 유효성검증 스크립트 입니다.
 */

/**
 * 카테고리를 검증합니다.
 * 카테고리는 필수항목 입니다.
 */
export const validCategory = (category) => {
  return category.length !== 0
      ? ''
      : '카테고리는 필수항목 입니다.';
}

/**
 * 작성자를 검증합니다.
 * 3글자이상 5글자 미만
 */
export const validWriter = (writer) => {
  return (writer.length >= 3 && writer.length < 5)
      ? ''
      : '4글자 이상, 16글자 미만 이여야 합니다.'
}

/**
 * 비밀번호를 검증합니다.
 * 4글자 이상, 16글자 미만 이여야 합니다.
 * 영문/숫자/특수문자를 포함 하여야 합니다.
 */
export const validPassword = (password) => {
  const lengthCondition = password.length >= 4 && password.length < 16;

  const pwdRex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#$%^&+=!]).*$/;
  const rexCondition = pwdRex.test(password);

  if (!lengthCondition) {
    return '4글자 이상, 16글자 미만 이여야 합니다.';
  } else if (!rexCondition) {
    return '영문/숫자/특수문자를 포함 하여야 합니다.';
  }

  return '';
}

export const validConfirm = (password, confirmPassword) => {
  return password === confirmPassword
      ? ''
      : '비밀번호가 서로 맞지 않습니다.';
}

/**
 * 제목을 검증합니다.
 * 4글자 이상, 100글자 미만 이여야 합니다.
 */
export const validTitle = (title) => {
  return title.length >= 4 && title.length < 100
      ? ''
      : '4글자 이상, 100글자 미만 이여야 합니다.';
}

/**
 * 내용을 검증합니다.
 * 4글자 이상, 2000글자 미만 이여야 합니다
 */
export const validContent = (content) => {
  return content.length >= 4 && content.length < 2000
      ? ''
      : '4글자 이상, 2000글자 미만 이여야 합니다';
}