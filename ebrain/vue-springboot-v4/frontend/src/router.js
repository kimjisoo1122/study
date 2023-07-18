import { createWebHistory, createRouter } from "vue-router";
import BoardList from "@/components/board/BoardList.vue";
import BoardDetail from "@/components/board/BoardDetail.vue";

const routes = [
  {
    path: '/board',
    component: BoardList
  },
  {
    path: '/board/:boardId',
    component: BoardDetail
  }
  // {
  //   path: "/list",
  //   component: List
  // },
  // {
  //   path: '/',
  //   component: Home
  // },
  // {
  //   // 정규식으로 엄중하게가능
  //   // * 입력하면 반복
  //   path: '/detail/:id(\\d+)',
  //   // redirection:
  //   component: Detail,
  //   children: [
  //     {
  //       path: 'author', // / 슬리스는 home이라는 뜻을 내포하기에 중첩라우터는 / 제거
  //       // component: Author.vue  -> /detail/{id}/author
  //     }
  //
  //
  //   ]
  // }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;