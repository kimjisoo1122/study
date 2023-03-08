<template>
  <div v-if="모달창열렸니" class="black-bg">
    <div class="white-bg">
      <div class=white-bg-img>
        <img :src="원룸들[누른거].image" alt="">
      </div>
      <h4>{{ 원룸들[누른거].title }}</h4>
      <p>{{ 원룸들[누른거].content }}</p>
      <p>{{ 원룸들[누른거].price }} 원</p>
      <button @click="모달창열렸니 = false"> X</button>
    </div>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Modal"
}
</script>

<style scoped>

</style>