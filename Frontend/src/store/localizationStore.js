import {defineStore} from 'pinia'

export const useLocalization = defineStore('localize', {
    state: () => {
        return {
            language: 'fa',
            flag : '🇮🇷',
            locals: {
                fa: {
                    username: 'نام‌کاربری',
                    password: 'رمز‌عبور',
                    signIn: 'ورود',
                    signOut: 'خروج',
                    dashboard: 'داشبورد',
                    servers: 'سرورها',
                    customers: 'مشتری‌ها',
                    subscriptions: 'اشتراک‌ها',
                    new: 'جدید',
                    edit: 'ویرایش',
                    delete: 'حذف',
                    update: 'بروزرسانی',
                },
                en: {
                    username: 'Username',
                    password: 'Password',
                    signIn: 'Sign in',
                    signOut: 'Sign out',
                    dashboard: 'Dashboard',
                    servers: 'Servers',
                    customers: 'Customers',
                    subscriptions: 'Subscriptions',
                    new: 'new',
                    edit: 'Edit',
                    delete: 'Delete',
                    update: 'Update',
                },
            }
        }
    },
    getters: {
        getLanguage() {
            return this.language
        },
        getFlag(){
          return this.flag
        },
        getLocal() {
            return this.locals[this.language]
        },
    },
    actions: {
        changeLanguage(payload) {
            this.language = payload[1]
            this.flag = payload[0]
        },
    }
})