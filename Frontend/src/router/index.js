import {createRouter, createWebHistory} from 'vue-router'
import login from '../views/login/index.vue'
import adminDash from '../views/admin/dashboard/index.vue'
import adminServersPage from '../views/admin/servers/index.vue'
import adminSubscriptionsPage from '../views/admin/subscriptions/index.vue'
import adminCustomersPage from '../views/admin/customers/index.vue'
import adminsListPage from '../views/admin/admins/index.vue'
import plansListPage from '../views/admin/plans/index.vue'
import customersPage from '../views/customer/index.vue'


const routes = [
    {
        path: '/',
        component: login
    },
    {
        path: '/admin',
        children: [
            {
                path: '',
                component: adminDash
            },
            {
                path: 'servers',
                component: adminServersPage
            },
            {
                path: 'subscriptions',
                component: adminSubscriptionsPage
            },
            {
                path: 'customers',
                component: adminCustomersPage
            },
            {
                path: 'admins',
                component: adminsListPage
            },
            {
                path: 'plans',
                component: plansListPage
            },
        ]
    },
    {
        path: '/customer',
        children: [
            {
                path: '',
                component: customersPage
            },
        ]
    },
]

const router = createRouter({
    // 4. Provide the history implementation to use. We are using the hash history for simplicity here.
    history: createWebHistory(),
    routes, // short for `routes: routes`
})

export default router