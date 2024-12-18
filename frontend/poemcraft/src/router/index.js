import { createRouter, createWebHistory } from 'vue-router';
import poemCraft from '../components/poemCraft.vue'
import FeedBack from '../components/FeedBack.vue'
import AccountPage from '../components/AccountPage.vue';
import FavoritesPage from '../components/FavoriteList.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../components/RegistrationForm.vue')
  },
  {
    path: '/poem',
    name: 'poem',
    component: poemCraft
  },
  {
    path: '/feedback',
    name: 'feedback',
    component: FeedBack,
  },
  {
    path: '/account',
    name: 'AccountPage',
    component: AccountPage,
  },
  {
    path: '/favorites',
    name: 'FavoritesPage',
    component: FavoritesPage,
  },
];


const router = createRouter({
  history: createWebHistory(), 
  routes
});

export default router;
