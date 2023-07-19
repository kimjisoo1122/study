/**
 * 자바 LocalDateTime 형식의 문자열을 yyyy.MM.dd HH:mm 포맷으로 변경
 * @param date
 * @returns yyyy.MM.dd HH:mm 포맷
 */
export const formatDate = (date) => {
  return date.replace('T', ' ').replaceAll('-', '.').slice(0, -3);
}


