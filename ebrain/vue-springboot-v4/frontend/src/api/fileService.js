import axios from "axios";

/**
 * 파일다운로드를 요청합니다.
 */
export const fileDown = (fileId) => {
  return axios({
    url: `/api/file/${fileId}`,
    method: 'GET',
    responseType: 'blob',  // 바이너리데이터를 받을수있는 브라우저전용 데이터타입
  })
      .then((res) => {
        const url = window.URL.createObjectURL(new Blob([res.data]))
        const link = document.createElement('a')
        link.href = url
        link.download = extractDownloadFilename(res);

        document.body.appendChild(link)
        link.click();
        link.remove();

        window.URL.revokeObjectURL(url);
      })
      .catch(error => {
        if (error.response.data) {
          const reader = new FileReader();
          reader.onload = function() {
            const errorData = JSON.parse(reader.result);
            console.error(errorData.errorMessage);
            alert(errorData.errorMessage);
          };
          reader.readAsText(error.response.data);
        }
      })
};

/**
 * 다운로드 파일이름을 추출하는 함수
 * @param response
 * @returns 다운로드 파일이름
 */
function extractDownloadFilename(response) {
  const disposition = response.headers["content-disposition"];
  return decodeURI(
      disposition
          .match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1]
          .replace(/['"]/g, "")
  );
}