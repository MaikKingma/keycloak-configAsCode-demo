/*
  This example requires some changes to your config:

  ```
  // tailwind.config.js
  module.exports = {
    // ...
    plugins: [
      // ...
      require('@tailwindcss/forms'),
    ],
  }
  ```
*/
import { useConstCallback } from 'keycloakify/tools/useConstCallback'
import { I18n } from 'login/i18n'
import { KcContext } from 'login/kcContext'
import { FormEventHandler, useState } from 'react'

export default function EmailLoginForm(props: {
  kcContext: Extract<KcContext, { pageId: 'login.ftl' }>
  i18n: I18n
  loginAction: string
  selectedCredential?: string
}) {
  const { kcContext, i18n, loginAction, selectedCredential } = props
  const { realm, login, usernameEditDisabled, url } = kcContext
  const label = !realm.loginWithEmailAllowed
    ? 'username'
    : realm.registrationEmailAsUsername
    ? 'email'
    : 'usernameOrEmail'

  const autoCompleteHelper: typeof label =
    label === 'usernameOrEmail' ? 'username' : label

  const displayRememberMe = realm.rememberMe && !usernameEditDisabled
  const displayResetPassword = realm.resetPasswordAllowed

  const { msg } = i18n

  const [isLoginButtonDisabled, setIsLoginButtonDisabled] = useState(false)

  const onSubmit = useConstCallback<FormEventHandler<HTMLFormElement>>((e) => {
    e.preventDefault()

    setIsLoginButtonDisabled(true)

    const formElement = e.target as HTMLFormElement

    //NOTE: Even if we login with email Keycloak expect username and password in
    //the POST request.
    formElement
      .querySelector("input[name='email']")
      ?.setAttribute('name', 'username')

    formElement.submit()
  })
  return (
    <>
      {/*
        This example requires updating your template:

        ```
        <html class="h-full bg-white">
        <body class="h-full">
        ```
      */}
      <div>
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <form
            className="space-y-6"
            action={loginAction}
            method="POST"
            id="kc-form-login"
            onSubmit={onSubmit}
          >
            <div>
              <label
                htmlFor={autoCompleteHelper}
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                {msg(label)}
              </label>
              <div className="mt-2">
                <input
                  id={autoCompleteHelper}
                  name={autoCompleteHelper}
                  type={label.startsWith('username') ? 'text' : 'email'}
                  tabIndex={1}
                  defaultValue={login.username ?? ''}
                  autoComplete={label.startsWith('email') ? 'email' : 'off'}
                  required
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 px-2"
                  {...(usernameEditDisabled
                    ? { disabled: true }
                    : {
                        autoFocus: true,
                        autoComplete: 'off',
                      })}
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label
                  htmlFor="password"
                  className="block text-sm font-medium leading-6 text-gray-900"
                >
                  {msg('password')}
                </label>
                {displayResetPassword && (
                  <div className="text-sm">
                    <a
                      tabIndex={5}
                      href={url.loginResetCredentialsUrl}
                      className="text-gray-500 hover:text-papaya underline decoration-papaya"
                    >
                      {msg('doForgotPassword')}
                    </a>
                  </div>
                )}
              </div>
              <div className="mt-2">
                <input
                  tabIndex={2}
                  id="password"
                  name="password"
                  type="password"
                  autoComplete="current-password"
                  required
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 px-2"
                />
              </div>
            </div>

            {displayRememberMe && (
              <div className="flex items-center">
                <input
                  tabIndex={3}
                  id="rememberMe"
                  name="rememberMe"
                  type="checkbox"
                  className="h-4 w-4 rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                  {...(login.rememberMe
                    ? {
                        checked: true,
                      }
                    : {})}
                />
                <label
                  htmlFor="rememberMe"
                  className="ml-2 min-w-0 flex-1 text-gray-500 text-sm"
                >
                  {msg('rememberMe')}
                </label>
              </div>
            )}

            <div>
              <input
                type="hidden"
                id="id-hidden-input"
                name="credentialId"
                {...(selectedCredential !== undefined
                  ? {
                      value: selectedCredential,
                    }
                  : {})}
              />
              <button
                tabIndex={4}
                type="submit"
                className="flex w-full justify-center rounded-full bg-papaya px-3 py-1.5 text-sm font-semibold leading-6 text-black shadow-sm hover:bg-orange-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-orange-600"
                disabled={isLoginButtonDisabled}
              >
                {msg('doLogIn')}
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  )
}
