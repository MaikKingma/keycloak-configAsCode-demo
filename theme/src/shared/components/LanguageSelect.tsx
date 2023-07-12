import { Listbox, Transition } from '@headlessui/react'
import { I18n } from 'login/i18n'
import { KcContext } from 'login/kcContext'
import React, { Fragment } from 'react'

function classNames(...classes: string[]) {
  return classes.filter(Boolean).join(' ')
}

function ChevronUpDownIcon(props: React.ComponentProps<'svg'>) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      className="h-5 w-5 text-gray-400"
      viewBox="0 0 20 20"
      fill="currentColor"
      aria-hidden="true"
    >
      <path
        fillRule="evenodd"
        d="M5.293 7.293a1 1 0 0 1 1.414 0L10
      10.586l3.293-3.293a1 1 0 1 1 1.414 1.414l-4
      4a1 1 0 0 1-1.414 0l-4-4a1 1 0 0
      0-1.414 0 1 1 0 0 1 0-1.414z"
        clipRule="evenodd"
      />
    </svg>
  )
}

export default function LanguageSelect(props: {
  kcContext: KcContext
  i18n: I18n
  showLabel?: boolean
}) {
  const { changeLocale, labelBySupportedLanguageTag, currentLanguageTag } =
    props.i18n
  const { locale } = props.kcContext

  const currentLanguageLabel = labelBySupportedLanguageTag[currentLanguageTag]

  return (
    <Listbox value={currentLanguageTag} onChange={changeLocale}>
      {({ open }) => (
        <>
          {props.showLabel ? (
            <Listbox.Label className="block text-sm font-medium leading-6 text-gray-900">
              Language
            </Listbox.Label>
          ) : (
            ''
          )}
          <div className="relative mt-2">
            <Listbox.Button className="relative w-full cursor-default rounded-md bg-white py-1.5 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-500 sm:text-sm sm:leading-6">
              <span className="flex items-center">
                <span className="ml-3 block truncate">
                  {currentLanguageLabel}
                </span>
              </span>
              <span className="pointer-events-none absolute inset-y-0 right-0 ml-3 flex items-center pr-2">
                <ChevronUpDownIcon />
              </span>
            </Listbox.Button>

            <Transition
              show={open}
              as={Fragment}
              leave="transition ease-in duration-100"
              leaveFrom="opacity-100"
              leaveTo="opacity-0"
            >
              <Listbox.Options className="absolute z-10 mt-1 max-h-56 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                {locale?.supported.map(({ languageTag }) => (
                  <Listbox.Option
                    key={languageTag}
                    value={languageTag}
                    className={({ active }) =>
                      classNames(
                        active ? 'bg-indigo-600 text-white' : 'text-gray-900',
                        'relative cursor-default select-none py-2 pl-3 pr-9'
                      )
                    }
                  >
                    {({ selected, active }) => (
                      <>
                        <div className="flex items-center">
                          <span
                            className={classNames(
                              selected ? 'font-semibold' : 'font-normal',
                              'ml-3 block truncate'
                            )}
                          >
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
  )
}
