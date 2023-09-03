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
                    firstName: 'نام',
                    lastName: 'نام‌خانوادگی',
                    email: 'ایمیل',
                    phoneNumber: 'شماره تماس',
                    address: 'آدرس',
                    signIn: 'ورود',
                    signOut: 'خروج',
                    dashboard: 'داشبورد',
                    servers: 'سرورها',
                    customers: 'مشتری‌ها',
                    customer: 'مشتری‌',
                    subscriptions: 'اشتراک‌ها',
                    subscription: 'اشتراک‌',
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
                    cancel: 'لغو',
                    show: 'نمایش',
                    url: 'لینک',
                    startEndDate: 'تاریخ شروع/پایان',
                    usage: 'مصرف',
                    renew: 'تمدید',
                    totalFlow : 'حجم کل',
                    ipLimit: 'تعداد کاربر',
                    periodLength: 'طول دوره (روز)'
                },
                en: {
                    username: 'Username',
                    password: 'Password',
                    firstName: 'First name',
                    lastName: 'Last name',
                    email: 'Email',
                    phoneNumber: 'Phone number',
                    address: 'Address',
                    signIn: 'Sign in',
                    signOut: 'Sign out',
                    dashboard: 'Dashboard',
                    servers: 'Servers',
                    customers: 'Customers',
                    customer: 'Customer',
                    subscriptions: 'Subscriptions',
                    subscription: 'Subscription',
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
                    cancel: 'Cancel',
                    show: 'Show',
                    url: 'Url',
                    startEndDate: 'Start/End Date',
                    usage: 'Usage',
                    renew: 'renew',
                    totalFlow : 'Total flow',
                    ipLimit: 'Ip limit',
                    periodLength: 'Period length (days)'
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