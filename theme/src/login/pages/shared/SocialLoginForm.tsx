import { KcContext } from 'keycloakify/login/kcContext'

export default function SocialLoginForm(props: {
  providers: KcContext.Login['social']['providers']
}) {
  const { providers } = props
  return (
    <div
      id="kc-social-providers"
      // className={clsx(
      //     getClassName('kcFormSocialAccountContentClass'),
      //     getClassName('kcFormSocialAccountClass')
      // )}
    >
      <ul
      // className={clsx(
      //     getClassName('kcFormSocialAccountListClass'),
      //     providers.length > 4 &&
      //     getClassName('kcFormSocialAccountDoubleListClass')
      // )}
      >
        {providers?.map((p) => (
          <li
            key={p.providerId}
            // className={getClassName('kcFormSocialAccountListLinkClass')}
          >
            <a
              href={p.loginUrl}
              id={`zocial-${p.alias}`}
              // className={clsx('zocial', p.providerId)}
            >
              <span>{p.displayName}</span>
            </a>
          </li>
        ))}
      </ul>
    </div>
  )
}
