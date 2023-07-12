// Copy pasted from: https://github.com/InseeFrLab/keycloakify/blob/main/src/login/Template.tsx

import { assert } from 'keycloakify/tools/assert'
import { clsx } from 'keycloakify/tools/clsx'
import { usePrepareTemplate } from 'keycloakify/lib/usePrepareTemplate'
import { type TemplateProps } from 'keycloakify/login/TemplateProps'
import { useGetClassName } from 'keycloakify/login/lib/useGetClassName'
import PageBackground from 'login/pages/shared/PageBackground'
import LanguageSelect from 'shared/components/LanguageSelect'
import type { KcContext } from './kcContext'
import type { I18n } from './i18n'

export default function Template(props: TemplateProps<KcContext, I18n>) {
  const {
    displayInfo = false,
    displayMessage = true,
    displayRequiredFields = false,
    displayWide = false,
    showAnotherWayIfPresent = true,
    headerNode,
    showUsernameNode = null,
    infoNode = null,
    kcContext,
    i18n,
    doUseDefaultCss,
    classes,
    children,
  } = props

  const { getClassName } = useGetClassName({ doUseDefaultCss, classes })

  const { msg } = i18n

  const { realm, locale, auth, url, message, isAppInitiatedAction } = kcContext

  const { isReady } = usePrepareTemplate({
    doFetchDefaultThemeResources: doUseDefaultCss,
    url,
    stylesCommon: [
      // "node_modules/patternfly/dist/css/patternfly.min.css",
      // "node_modules/patternfly/dist/css/patternfly-additions.min.css",
      // "lib/zocial/zocial.css"
    ],

    styles: ['css/login.css'],
    htmlClassName: getClassName('kcHtmlClass'),
    bodyClassName: 'min-h-screen grid bg-gray-50',
  })

  if (!isReady) {
    return null
  }

  return (
    <div className="grid min-h-full">
      <div className="grid-stack overflow-hidden vertical-middle">
        <PageBackground />
      </div>
      <div className="grid-stack flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
        <nav className="flex justify-end min-w-full px-4">
          {realm.internationalizationEnabled &&
            (assert(locale !== undefined), true) &&
            locale.supported.length > 1 && (
              <div id="kc-locale">
                <div
                  id="kc-locale-wrapper"
                  className={getClassName('kcLocaleWrapperClass')}
                >
                  <LanguageSelect kcContext={kcContext} i18n={i18n} />
                </div>
              </div>
            )}
        </nav>
        <div id="kc-header" className="sm:mx-auto sm:w-full sm:max-w-sm">
          <div
            id="kc-header-wrapper"
            className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-white"
          >
            {msg('loginTitleHtml', realm.displayNameHtml)}
          </div>
        </div>

        <div className="w-full sm:w-96 sm:mx-auto sm:mt-8 sm:mb-12 bg-white shadow border rounded-lg">
          <header className={getClassName('kcFormHeaderClass')}>
            {!(
              auth !== undefined &&
              auth.showUsername &&
              !auth.showResetCredentials
            ) ? (
              displayRequiredFields ? (
                <div className={getClassName('kcContentWrapperClass')}>
                  <div
                    className={clsx(
                      getClassName('kcLabelWrapperClass'),
                      'subtitle'
                    )}
                  >
                    <span className="subtitle">
                      <span className="required">*</span>
                      {msg('requiredFields')}
                    </span>
                  </div>
                  <div className="col-md-10">
                    <h1
                      id="kc-page-title"
                      className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900"
                    >
                      {headerNode}
                    </h1>
                  </div>
                </div>
              ) : (
                <h1
                  id="kc-page-title"
                  className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900"
                >
                  {headerNode}
                </h1>
              )
            ) : displayRequiredFields ? (
              <div className={getClassName('kcContentWrapperClass')}>
                <div
                  className={clsx(
                    getClassName('kcLabelWrapperClass'),
                    'subtitle'
                  )}
                >
                  <span className="subtitle">
                    <span className="required">*</span> {msg('requiredFields')}
                  </span>
                </div>
                <div className="col-md-10">
                  {showUsernameNode}
                  <div className={getClassName('kcFormGroupClass')}>
                    <div id="kc-username">
                      <label id="kc-attempted-username">
                        {auth?.attemptedUsername}
                      </label>
                      <a id="reset-login" href={url.loginRestartFlowUrl}>
                        <div className="kc-login-tooltip">
                          <i className={getClassName('kcResetFlowIcon')}></i>
                          <span className="kc-tooltip-text">
                            {msg('restartLoginTooltip')}
                          </span>
                        </div>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            ) : (
              <>
                {showUsernameNode}
                <div className={getClassName('kcFormGroupClass')}>
                  <div id="kc-username">
                    <label id="kc-attempted-username">
                      {auth?.attemptedUsername}
                    </label>
                    <a id="reset-login" href={url.loginRestartFlowUrl}>
                      <div className="kc-login-tooltip">
                        <i className={getClassName('kcResetFlowIcon')}></i>
                        <span className="kc-tooltip-text">
                          {msg('restartLoginTooltip')}
                        </span>
                      </div>
                    </a>
                  </div>
                </div>
              </>
            )}
          </header>
          <div id="kc-content">
            <div
              id="kc-content-wrapper"
              className="flex min-h-full flex-1 flex-col px-6 pb-12 pt-2 lg:px-8"
            >
              {/* App-initiated actions should not see warning messages about the need to complete the action during login. */}
              {displayMessage &&
                message !== undefined &&
                (message.type !== 'warning' || !isAppInitiatedAction) && (
                  <div className={clsx('alert', `alert-${message.type}`)}>
                    {message.type === 'success' && (
                      <span
                        className={getClassName('kcFeedbackSuccessIcon')}
                      ></span>
                    )}
                    {message.type === 'warning' && (
                      <span
                        className={getClassName('kcFeedbackWarningIcon')}
                      ></span>
                    )}
                    {message.type === 'error' && (
                      <span
                        className={getClassName('kcFeedbackErrorIcon')}
                      ></span>
                    )}
                    {message.type === 'info' && (
                      <span
                        className={getClassName('kcFeedbackInfoIcon')}
                      ></span>
                    )}
                    <span
                      className="kc-feedback-text"
                      dangerouslySetInnerHTML={{
                        __html: message.summary,
                      }}
                    />
                  </div>
                )}
              {children}
              {auth !== undefined &&
                auth.showTryAnotherWayLink &&
                showAnotherWayIfPresent && (
                  <form
                    id="kc-select-try-another-way-form"
                    action={url.loginAction}
                    method="post"
                    className={clsx(
                      displayWide && getClassName('kcContentWrapperClass')
                    )}
                  >
                    <div
                      className={clsx(
                        displayWide && [
                          getClassName('kcFormSocialAccountContentClass'),
                          getClassName('kcFormSocialAccountClass'),
                        ]
                      )}
                    >
                      <div className={getClassName('kcFormGroupClass')}>
                        <input type="hidden" name="tryAnotherWay" value="on" />
                        {/* eslint-disable-next-line jsx-a11y/anchor-is-valid */}
                        <a
                          href="#"
                          id="try-another-way"
                          onClick={() => {
                            document.forms[
                              'kc-select-try-another-way-form' as never
                            ].submit()
                            return false
                          }}
                        >
                          {msg('doTryAnotherWay')}
                        </a>
                      </div>
                    </div>
                  </form>
                )}
              {displayInfo && (
                <div id="kc-info" className={getClassName('kcSignUpClass')}>
                  <div
                    id="kc-info-wrapper"
                    className={getClassName('kcInfoAreaWrapperClass')}
                  >
                    {infoNode}
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
