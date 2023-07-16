<template>

  <div class="menu">
    <!-- :key는 항상 v-for을 쓸때 필수인데 key에는 매태그 마다 변하는 유니크한 값이 들어가야함 idx를 주로 씀 -->
    <a v-for="(메뉴, idx) in 메뉴들" :key="idx">
      {{ 메뉴 }}
    </a>
  </div>

  <Discount v-if="showDiscount" :discountRate="discountRate"/>

  <button @click="sort">가격순정렬</button>

  <transition name="fade">
<!--  <div class="start" :class="{end : 모달창}">-->
    <!-- 부모에서 보내는 props는 read only임 해결방법 -> 자식이 $emit(이름,메시지) 보내서 부모가 수신해서 작업진행 -->
    <Modal :원룸들="원룸들" :누른거="누른거" @closeModal="모달창 = false" v-if="모달창"></Modal>
<!--  </div>-->
  </transition>


  <Card v-for="(원룸, i) in 원룸들" :key="i" :원룸="원룸" @openModal="모달창 = true; 누른거 = $event"></Card>

</template>

<script>

import {data} from "@/oneroom";
import Discount from "@/components/Discount.vue";
import Modal from "@/components/Modal.vue";
import Card from "@/components/Card.vue";

export default {
  name: 'App',
  // 데이터 보관함 return안에 오브젝트 형태로
  // 밑의 값은 데이터가 변경되면 관련된 html 실시간 자동 렌더링
  // Vue로 개발할때는 데이터를 중심으로 생각
  // 모든 데이터는 App.vue에서 관리하고 props로 전달해줌
  data(){
    return {
      원본원룸들 : [...data], // 사본만들기 array = 는 왼쪽 오른쪽 값을 공유하는 뜻
      메뉴들 : ['Home', 'Shop', 'About'],
      모달창 : false,
      원룸들 : data,
      누른거 : 0,
      showDiscount :  true,
      discountRate : 5,
    }
  },
  methods : {
    // this = 메소드를 포함하고 있는 object를 뜻함
    // increase(idx) {
    //   this.products[idx].신고수++;
    // }
    sort() {
      this.원룸들.sort((a, b) => {
        return a.price - b.price;
        // 음수가나오면
      })
    },
  },
  // 컴포넌트가 html에 장착되고 다보여줄 준비가 됬으면
  // 서버에서 데이터 가져올때도 라이프사이클 훅에서 함
  // ajax 요청할떄 create(html생성전), mounted(생성후)
  mounted() {
    // 화살표함수는 밖의 this를 그대로 씀 function은 window를 가리킴


    let interval =
        setInterval(() => {
          this.discountRate--;
          if (this.discountRate === 0) {
            clearInterval(interval);
            this.showDiscount = false;
          }
        }, 1000);

  },
  components: {
    Card,
    Modal,
    Discount
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.menu {
  background: darkslateblue;
  padding: 15px;
  border-radius: 5px;
}

.menu a {
  color: white;
  padding: 10px;
  text-decoration: none;
}

div {
  box-sizing: border-box;
  /* 모달의 width를 100%으로 하면 전체화면이 width가 1024px면
     1024로 한다음 우리가 그다음 padding을 줘버리면 기본적으로 box-sizing은 content-box가  디폴트 이기에
    컨텐츠만 1024px로 설정한다는 내용임 이러면 padding크기만큼 1024px에 더해지는 크기가 발생
    이때 border-box를하면 패딩이고 마진이고 보더크기고 나발이고 다합쳐서 1024px로 하겠다는거임.
    그래서 여기서 보더박스를 해야 화면에서 오른쪽과 아래가 패딩이 보이기시작함
    1024px에서 패딩을 줬는데 패딩만큼 오른쪽과 밑으로 늘어나는데 화면크기를 넘어가잖아
    이때 보더박스를주면 1024로 고정이되니 패딩이 보이기시작!
  */
}

/**
 스무스한 애니메이션효과 주는 법 시작 모습 클래스명, 마지막모습 클래스명
 transition all 1s -> 해당 클래스의 css중 all 어떤거라고 변경되면 1s에 따라 바뀌게함
 */
.start {
  opacity: 0;
  transition: all 1s;
}

.end {
  opacity: 1;
}

/*<transition> name 지정하고 정의*/
.fade-enter-from {
  transform: translateY(-1000px);
}
.fade-enter-active {
  transition: all 1s;
}
.fade-enter-to {
  transform: translateY(0);
}

/*퇴장애니메이션*/
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
