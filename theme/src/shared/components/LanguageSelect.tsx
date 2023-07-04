import { useGetClassName } from "keycloakify/login/lib/useGetClassName";
import {I18n} from "login/i18n";
import {KcContext} from "login/kcContext";

export default function LanguageSelect(props: { kcContext: KcContext, i18n: I18n }) {
    const { kcContext, i18n } = props;
    const { changeLocale, labelBySupportedLanguageTag, currentLanguageTag } = i18n;
    const { locale } = kcContext;
    const { getClassName } = useGetClassName({ doUseDefaultCss: true, classes: undefined });

    return <div id="kc-locale">
        <div id="kc-locale-wrapper" className={getClassName("kcLocaleWrapperClass")}>
            <div className="kc-dropdown" id="kc-locale-dropdown">
                {/* eslint-disable-next-line jsx-a11y/anchor-is-valid */}
                <a href="#" id="kc-current-locale-link">
                    {labelBySupportedLanguageTag[currentLanguageTag]}
                </a>
                <ul>
                    {locale?.supported.map(({ languageTag }) => (
                        <li key={languageTag} className="kc-dropdown-item">
                            {/* eslint-disable-next-line jsx-a11y/anchor-is-valid */}
                            <a href="#" onClick={() => changeLocale(languageTag)}>
                                {labelBySupportedLanguageTag[languageTag]}
                            </a>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    </div>
}
