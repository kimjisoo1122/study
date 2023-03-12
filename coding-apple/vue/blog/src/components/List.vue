<template>
  <div v-for="(blog, i) in data" :key="i">
    <h4>{{blog.title}}</h4>
    <p>{{blog.content}}</p>
  </div>
</template>

<script>
export default {
  name: "List",
  props: {
    data: Array
  }
}
</script>

<style scoped>

</style>