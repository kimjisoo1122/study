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
           @change="submitFile($event)">
  </div>
  <span class="file-error"></span>

</template>

<script>
export default {
  name: "FileInput",

  data() {
    return {
      fileName: '',
    }
  },

  props: {
    fileId: Number,
  },

  methods: {
    /**
     * 파일객체를 상위 컴포넌트에 전송합니다
     * @param e 이벤트
     */
    submitFile(e) {
      const file = e.target.files[0];
      this.fileName = file.name;
      this.$emit('submitFile', file);
    },
  }
}
</script>

<style scoped>

  .file-input-container {
    display: flex;
    gap: 5px;
    margin: 5px 0 5px 10px;
  }

  .file-upload-label {
    border: 1px solid black;
    background-color: lightgray;
    cursor: pointer;
    padding: 4px 5px;
    font-size: 13px;
  }

  .file-disabled {
    width: 300px;
    background-color: white;
    border: 1px solid black;
    padding: 0 3px;
  }

  .file-upload {
    display: none;
  }

</style>