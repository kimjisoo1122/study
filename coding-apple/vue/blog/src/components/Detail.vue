<template>
  <div>
    <h4 @click="$router.push('/detail/0')">{{ data[$route.params.id].title }}</h4>
    <p>{{ data[$route.params.id].content }}</p>
  </div>
</template>

<script>
export default {
  name: "Detail",
  data() {
    // id: $route.params.id
  },
  props: {
    data: Array
  },
}
</script>

<style scoped>

</style>