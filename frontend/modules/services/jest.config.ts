export default {
  displayName: 'Celfocus',
  preset: '../../jest.preset.js',
  transform: {
    '^(?!.*\\.(js|ts|json)$)': 'ts-jest',
    '^.+\\.[tj]sx?$': ['babel-jest', { presets: ['@nx/js/babel'] }],
  },
  moduleFileExtensions: ['ts', 'tsx', 'js', 'jsx'],
  coverageDirectory: '../../coverage/apps/Celfocus',
};
