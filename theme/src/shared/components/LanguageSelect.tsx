import {Listbox, Transition} from "@headlessui/react";
import {useGetClassName} from "keycloakify/login/lib/useGetClassName";
import {I18n} from "login/i18n";
import {KcContext} from "login/kcContext";
import React, {Fragment} from "react";

function classNames(...classes: string[]) {
    return classes.filter(Boolean).join(' ')
}

export default function LanguageSelect(props: { kcContext: KcContext, i18n: I18n }) {
    const {kcContext, i18n} = props;
    const {changeLocale, labelBySupportedLanguageTag, currentLanguageTag} = i18n;
    const {locale} = kcContext;
    const {getClassName} = useGetClassName({doUseDefaultCss: true, classes: undefined});
    const currentLanguageLabel = labelBySupportedLanguageTag[currentLanguageTag]

    return <div id="kc-locale">
        <div id="kc-locale-wrapper" className={getClassName("kcLocaleWrapperClass")}>
        <Listbox value={currentLanguageTag} onChange={changeLocale}>
            {({open}) => (
                <>
                    <Listbox.Label className="block text-sm font-medium leading-6 text-gray-900">Language</Listbox.Label>
                    <div className="relative mt-2">
                        <Listbox.Button className="relative w-full cursor-default rounded-md bg-white py-1.5 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 sm:text-sm sm:leading-6">
                          <span className="flex items-center">
                            <span className="ml-3 block truncate">{currentLanguageLabel}</span>
                          </span>
                          {/*<span className="pointer-events-none absolute inset-y-0 right-0 ml-3 flex items-center pr-2">*/}
                          {/*  <ChevronUpDownIcon className="h-5 w-5 text-gray-400" aria-hidden="true" />*/}
                          {/*</span>*/}
                        </Listbox.Button>

                        <Transition
                            show={open}
                            as={Fragment}
                            leave="transition ease-in duration-100"
                            leaveFrom="opacity-100"
                            leaveTo="opacity-0"
                        >
                            <Listbox.Options className="absolute z-10 mt-1 max-h-56 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                                {locale?.supported.map(({languageTag}) => (
                                    <Listbox.Option
                                        key={languageTag}
                                        value={languageTag}
                                        className={({active}) =>
                                            classNames(
                                                active ? 'bg-indigo-600 text-white' : 'text-gray-900',
                                                'relative cursor-default select-none py-2 pl-3 pr-9'
                                            )
                                        }
                                    >
                                        {({selected, active}) => (
                                            <>
                                                <div className="flex items-center">
                                                    <span className={classNames(selected ? 'font-semibold' : 'font-normal', 'ml-3 block truncate')}>
                                                        {labelBySupportedLanguageTag[languageTag]}
                                                    </span>
                                                </div>
                                                {/*{selected ? (*/}
                                                {/*    <span*/}
                                                {/*        className={classNames(*/}
                                                {/*            active ? 'text-white' : 'text-indigo-600',*/}
                                                {/*            'absolute inset-y-0 right-0 flex items-center pr-4'*/}
                                                {/*        )}*/}
                                                {/*    >*/}
                                                {/*        /!*<CheckIcon className="h-5 w-5" aria-hidden="true" />*!/*/}
                                                {/*    </span>*/}
                                                {/*) : null}*/}
                                            </>
                                        )}
                                    </Listbox.Option>
                                ))}
                            </Listbox.Options>
                        </Transition>
                    </div>
                </>
            )}
        </Listbox>
        </div>
    </div>
}
