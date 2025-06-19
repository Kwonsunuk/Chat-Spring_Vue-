<!-- chat-frontend/layouts/default.vue -->
<template>
  <!-- 전체 화면 꽉 차는 컨테이너-->
  <div class="container-fluid p-0">
    <!-- 가운데 정렬된 레이아웃 -->
    <div class="container">
      <!-- row: 가로 정렬, gx-0: 수평 여백 제거 -->
      <div class="row gx-0">
        <!-- 왼쪽 사이드바: 친구/유저 정보 -->
        <div class="col-md-3 mb-4">
          <client-only>
            <Sidebar />
          </client-only>
          <!-- 사용자 정보 밑 즐겨찾기한 친구 목록과 친구들 -->
        </div>
        <!-- 오른쪽 메인 콘텐츠 -->
        <div class="col-md-9">
          <NuxtPage />
        </div>
      </div>
    </div>
    <div class="container">
      <!-- 하단 메뉴바 -->
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';

import { useUserStore } from '@/stores/user';
import { Sidebar } from '#components';

const userStore = useUserStore();

onMounted(async () => {
  if (!userStore.isAuthenticated) {
    const { data } = await useFetch('http://localhost:8080/api/users/me', {
      credentials: 'include',
    });
    if (data.value?.includes('현재 로그인한 사용자')) {
      const name = data.value.split(': ')[1];
      userStore.login(name);
    }
  }
});
</script>
