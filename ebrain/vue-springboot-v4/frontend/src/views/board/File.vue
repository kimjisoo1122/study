<template>

  <div class="file-container">

    <router-link v-for="file in files"
                 :key="file.fileId"
                 class="file-router"
                 :to="`/file/${file.fileId}`" >
      {{ file.physicalName }}
    </router-link>

  </div>

</template>

<script>
export default {
  name: "File",
  props: {
    files: Array
  }
}
</script>

<style scoped>

  .file-container {
    display: flex;
    flex-direction: column;
    gap: 3px;
    margin-bottom: 10px;
    padding: 10px;
  }

  .file-router {
    font-size: 14px;
    text-decoration: navajowhite;
  }

</style>