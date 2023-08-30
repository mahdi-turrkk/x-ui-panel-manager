import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: '/',
        component: () => import('../views/login/index.vue')
    },
    {
        path: '/admin',
        children: [
            {
                path: '',
                component: () => import('../views/admin/dashboard/index.vue')
            },
            {
                path: 'servers',
                component: () => import('../views/admin/servers/index.vue')
            },
            {
                path: 'subscriptions',
                component: () => import('../views/admin/subscriptions/index.vue')
            },
            {
                path: 'customers',
                children: [
                    {
                        path: '',
                        component: () => import('../views/admin/customers/index.vue')
                    },
                    {
                        path: ':id',
                        component: () => import('../views/admin/customers/customerDetail/index.vue')
                    }
                ]
            },
        ]
    },
    {
        path: '/customer',
        children: [
            {
                path: '',
                component: () => import('../views/customer/dashboard/index.vue')
            },
            {
                path: 'subscriptions',
                component: () => import('../views/customer/subscriptions/index.vue')
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