// plugins/init-auth.client.js
import { useUserStore } from '@/stores/user';

export default defineNuxtPlugin(async () => {
  const userStore = useUserStore();

  // 로그인 상태가 이미 true면 굳이 요청하지 않음
  if (!userStore.isAuthenticated) {
    try {
      const { data } = await useFetch('http://localhost:8080/api/users/me', {
        credentials: 'include',
      });

      if (data.value?.includes('현재 로그인한 사용자')) {
        const name = data.value.split(': ')[1];
        userStore.login(name);
      }
    } catch (e) {
      // 인증 실패 또는 에러 무시
    }
  }
});
