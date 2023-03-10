<template>
  <transition name="fade">
  <div v-if="모달창열렸니" class="black-bg" >
    <div class="white-bg">
      <div class=white-bg-img>
        <img :src="원룸들[누른거].image" alt="">
      </div>
      <h4>{{ 원룸들[누른거].title }}</h4>
      <p>{{ 원룸들[누른거].content }}</p>
      <input v-model="month" >
      <textarea v-model="text"></textarea>
      <select v-model="month">
        <option>1</option>
        <option>2</option>

      </select>
      <p>{{month}}개월 선택함 {{ 원룸들[누른거].price * month }} 원</p>
      <button @click="$emit('closeModal')"> X </button>
    </div>
  </div>
  </transition>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Modal",
  data() {
    return {
      month: 1,
      text: ''
    }
  },
  watch:{
    // month(a){
    //   const myRe = /[\D\s]/g;
    //   if (myRe.test(a)) {
    //     alert('문자 또는 공백이 있습니다~')
    //   }
    //
    //   if (a > 12) {
    //     // console.log('12보다 크면안댐', typeof number);
    //   }
    // }
  },
  props: {
    원룸들: Array,
    누른거: Number,
    모달창열렸니: Boolean
  },
  updated() {
    console.log(this.month);
  }
}
</script>

<style scoped>
.black-bg {
  position: fixed;
  padding: 20px;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.white-bg {
  width: 35%;
  margin: 50px auto;
  /*height: 35%;*/
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  position: relative;

  /*position: absolute;*/
  /*top: 50%;*/
  /*left: 50%;*/
  /*transform: translate(-50%, -50%);*/
}

.white-bg button {
  padding: 10px 15px;
  background-color: white;
  border-radius: 3px;
  border: 1px solid lightgray;
  cursor: pointer;
  position: absolute;
  right: 10px;
  bottom: 10px;
}

.white-bg-img {
  width: 50%;
  margin: 10px auto;
  border-radius: 10px;
  overflow: hidden;
}

.white-bg-img img {
  width: 100%;
  display: block;
}

textarea {
  width: 70%;
  margin: 10px auto;
  resize: none;
  display: block;
}
input {
  width: 70%;
}
select {
  width: 50px
}

.fade-enter-from {
  transform: translateY(100vh);
}
.fade-enter-active {
  transition: all 1s;
}
.fade-enter-to {
  transform: translateY(0);
}

.fade-leave-from {
  opacity: 1;
}
.fade-leave-active {
  transition: all 1s;
}
.fade-leave-to {
  opacity: 0;
}
</style>