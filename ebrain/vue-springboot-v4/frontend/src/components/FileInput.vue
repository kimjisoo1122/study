<!-- 파일인풋 컴포넌트 입니다. -->
<template>

  <div class="file-input-container">
    <input type="text"
           class="file-disabled"
           v-model="fileName"
           disabled>
    <label :for="fileId" class="file-upload-label">파일 찾기</label>
    <input type="file"
           class="file-upload"
           :id="fileId"
           @change="submitFile">
    <span class="file-error">{{ fileError }}</span>
    <span v-if="serverFileError !== ''" class="file-error">{{ serverFileError }}</span>
  </div>


</template>

<script>
export default {
  name: "FileInput",

  data() {
    return {
      fileMaxSize: 1000 * 1000 * 10, // 파일최대크기 10MB
      fileName: '', // 파일이름
      fileError: '', // 파일에러
    }
  },

  props: {
    fileId: Number, // 파일번호
    serverFileError: String, // 서버파일에러
  },

  methods: {
    /**
     * 파일객체를 상위 컴포넌트에 전송합니다
     * @param event 이벤트
     */
    submitFile(event) {
      const file = event.target.files[0];

      if (file.size > this.fileMaxSize) {
        const formattedMaxSize = this.fileMaxSize / (1000 * 1000) + 'MB';
        this.fileError = `파일사이즈는 ${formattedMaxSize}를 넘길수 없습니다.`;
        return false;
      }

      this.fileName = file.name;
      this.$emit('submitFile', file);
    },
  }
}
</script>

<style scoped>

  .file-input-container {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
    margin: 5px 0;
  }

  .file-upload-label {
    border: 1px solid black;
    background-color: lightgray;
    cursor: pointer;
    font-size: 12px;
    height: 25px;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0 5px;
  }

  .file-disabled {
    box-sizing: border-box;
    width: 300px;
    height: 25px;
    background-color: white;
    border: 1px solid black;
  }

  .file-upload {
    display: none;
  }

  .file-error {
    font-size: 12px;
    color: red;
    flex-basis: 100%;
  }

</style>