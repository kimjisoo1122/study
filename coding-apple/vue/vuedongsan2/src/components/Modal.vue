<template>
  <div class="black-bg">
    <div class="white-bg">
      <div class="white-bg-image-container">
        <img :src="원룸들[누른거].image" alt="원룸사진">
      </div>
      <h4>{{원룸들[누른거].title}}</h4>
      <p>{{원룸들[누른거].content}}</p>
      <!--      <input type="text" @input="month = $event.target.value">-->
      <!-- v-model="" month에 저장시킴 위의 값의 축약 버전 v-모델은 초기값 값만 받을 수 있음-->
      <input type="text" v-model="month">
      <!-- 입력값을 숫자로 지정 -->
<!--      <input type="text" v-model.number="month">-->
      <!--  자바스크립트는 문자 + 숫자에서 -랑 *은 숫자인형태는 숫자로 인식 + 는 문자열로 인식함   -->
      <p> {{month}}개월 선택함 {{원룸들[누른거].price * month}} 원</p>
      <!-- form에서 input값의 유효성 검증을 할 경우 watcher를 쓰게 돼있음      -->
      <div class="close-container">
      <!-- 자식 컴포넌트에서 props를 변경하면 안됨 -->
        <button class="close-btn" @click="$emit('closeModal')">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Modal",
  data() {
    return {
      month : 1,
    }
  },
  watch: {
    // form validation 라이브러리 써도됨(vue 라이브러리)
    // 데이터명을 함수로 만들면 데이터를 감시함
    month(param) {
      // month값이 변경할때마다 함수실행 되는거임
      console.log(isNaN(param))
      if (isNaN(param)) {
        alert('숫자가 아닙니다.');
        this.month = 1;
      }
    },
  },
  props: {
    원룸들 : Array,
    모달창 : Boolean,
    누른거 : Number,
  }
}
</script>

<style scoped>

  .black-bg {
    position: fixed;
    background-color: rgba(0,0,0,0.5);
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    padding: 20px;
  }

  .white-bg {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 40%;
    height: 40%;
    margin-bottom: 50px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .close-container {
    position: absolute;
    right: 30px;
    bottom: 30px;
  }
  .close-btn {
    padding: 10px;
    cursor: pointer;
  }

  .white-bg-image-container {
    width: 300px;
    height: 100px;
  }

  .white-bg-image-container img {
    width: 100%;
    height: 100%;
    padding: 0;
    display: block;
  }
</style>