import {defineStore} from 'pinia'

export const useLocalization = defineStore('localize', {
    state: () => {
        return {
            language: 'fa',
            flag: '🇮🇷',
            direction: 'rtl',
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
                    add: 'افزودن',
                    id: 'شناسه',
                    serverUrl: 'آدرس سرور',
                    server: 'سرور',
                    information: 'اطلاعات',
                    generatable: 'قابلیت تولید',
                    actions: 'عملیات',
                    active: 'فعال',
                    activate: 'فعالسازی',
                    deactivate: 'غیرفعالسازی',
                    inactive: 'غیرفعال',
                    inbounds: 'دسته کانفیگ‌ها',
                    inbound: 'دسته کانفیگ',
                    title: 'عنوان',
                    port: 'پورت',
                    status: 'وضعیت',
                    save: 'ذخیره',
                    cancel: 'لغو'
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
                    add: 'Add',
                    id: 'ID',
                    serverUrl: 'Server url',
                    server: 'Server',
                    information: 'Information',
                    generatable: 'Generatable',
                    actions: 'Actions',
                    active: 'Active',
                    activate: 'Activate',
                    deactivate: 'Deactivate',
                    inactive: 'Inactive',
                    inbounds: 'Inbounds',
                    inbound: 'Inbound',
                    title: 'title',
                    port: 'port',
                    status: 'status',
                    save: 'Save',
                    cancel: 'Cancel'
                },
            }
        }
    },
    getters: {
        getLanguage() {
            return this.language
        },
        getFlag() {
            return this.flag
        },
        getLocal() {
            return this.locals[this.language]
        },
        getDirection() {
            return this.direction
        },
    },
    actions: {
        changeLanguage(payload) {
            this.flag = payload[0]
            this.language = payload[1]
            this.direction = payload[2]
        },
    }
})