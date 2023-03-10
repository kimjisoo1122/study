<template>
  <div class="discount" v-if="showDiscount">
    <h4>지금 결제하면 {{discountRate}}%할인</h4>
    <input v-model="month">
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Discount",
  data() {
    return {
      discountRate: 10,
      month: 1
    }
  },
  mounted() {
    console.log('discount');
    let interval = setInterval(() => {
      this.discountRate--;
      if (this.discountRate == 0) {
        clearInterval(interval);

      }
    }, 1000);
  },
  updated() {
    console.log(this.month);
  },
  props:{
    showDiscount: Boolean
  }
};
</script>

<style scoped>
.discount {
  background-color: #eee;
  padding: 10px;
  margin: 10px;
  border-radius: 5px;
}
</style>