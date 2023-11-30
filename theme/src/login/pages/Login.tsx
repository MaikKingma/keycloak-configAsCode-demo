// ejected using 'npx eject-keycloak-page'
import EmailLoginForm from 'login/pages/shared/EmailLoginForm'
import SocialLoginForm from 'login/pages/shared/SocialLoginForm'
import { useState, type FormEventHandler } from 'react'
import { clsx } from 'keycloakify/tools/clsx'
import { useConstCallback } from 'keycloakify/tools/useConstCallback'
import type { PageProps } from 'keycloakify/login/pages/PageProps'
import { useGetClassName } from 'keycloakify/login/lib/useGetClassName'
import type { KcContext } from '../kcContext'
import type { I18n } from '../i18n'

export default function Login(
  props: PageProps<Extract<KcContext, { pageId: 'login.ftl' }>, I18n>
) {
  const { kcContext, i18n, doUseDefaultCss, Template, classes } = props

  const { getClassName } = useGetClassName({
    doUseDefaultCss,
    classes,
  })

  const {
    social,
    realm,
    url,
    usernameHidden,
    login,
    auth,
    registrationDisabled,
  } = kcContext

  const { msg, msgStr } = i18n

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
    <Template
      {...{ kcContext, i18n, doUseDefaultCss, classes }}
      displayInfo={social.displayInfo}
      displayWide={realm.password && social.providers !== undefined}
      headerNode={msg('doLogIn')}
      infoNode={
        realm.password &&
        realm.registrationAllowed &&
        !registrationDisabled && (
          <div id="kc-registration">
            <span>
              {msg('noAccount')}
              <a tabIndex={6} href={url.registrationUrl}>
                {msg('doRegister')}
              </a>
            </span>
          </div>
        )
      }
    >
      <div id="kc-form">
        <div id="kc-form-wrapper">
          {realm.password && (
            <EmailLoginForm
              kcContext={kcContext}
              i18n={i18n}
              loginAction={url.loginAction}
              selectedCredential={auth.selectedCredential}
            />
          )}
        </div>
        {realm.password && social.providers !== undefined && (
          <SocialLoginForm providers={social.providers} />
        )}
      </div>
    </Template>
  )
}
