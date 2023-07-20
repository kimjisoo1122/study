<template>

  <div class="file-container">

    <router-link class="file-router"
                 :to="`/file/${file.fileId}`" >
      {{ file.originalName }}
    </router-link>

  </div>

</template>

<script>
export default {
  name: "File",
  props: {
    file: Object
  }
}
</script>

<style scoped>

  .file-container {
    padding: 3px 0;
    margin-bottom: 3px;
  }

  .file-router {
    font-size: 14px;
    text-decoration: navajowhite;
  }

  .file-router:visited {
    color: blue;
  }

</style>