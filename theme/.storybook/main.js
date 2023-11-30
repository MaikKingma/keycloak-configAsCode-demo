module.exports = {
  "stories": ["../src/**/*.stories.tsx"],
  "addons": ["@storybook/addon-links", "@storybook/addon-essentials", "@storybook/addon-interactions", "@storybook/preset-create-react-app"],
  "framework": {
    name: "@storybook/react-webpack5",
    options: {}
  },
  "staticDirs": ['../public'],
  docs: {
    autodocs: true
  }
};