# Contributing to Movie Locations

Thank you for your interest in contributing to the Movie Locations app!

## How to Contribute

### Reporting Bugs

1. Check if the bug has already been reported in Issues
2. If not, create a new issue with:
   - Clear title and description
   - Steps to reproduce
   - Expected vs actual behavior
   - Screenshots if applicable
   - Device/emulator details and Android version

### Suggesting Features

1. Open an issue with the "enhancement" label
2. Describe the feature and its benefits
3. Include mockups or examples if possible

### Code Contributions

#### Setup Development Environment

1. Fork the repository
2. Clone your fork locally
3. Follow SETUP.md to configure the project
4. Create a new branch: `git checkout -b feature/your-feature-name`

#### Coding Standards

- Follow existing code style
- Use meaningful variable and method names
- Add comments for complex logic
- Update documentation if needed
- Test on multiple devices/API levels

#### Before Submitting

- [ ] Code compiles without errors
- [ ] App runs without crashes
- [ ] New features are documented
- [ ] Strings are in strings.xml (not hardcoded)
- [ ] No API keys or secrets in code
- [ ] Code follows Android best practices

#### Pull Request Process

1. Update README.md if needed
2. Commit with clear message: `Add feature: description`
3. Push to your fork
4. Create Pull Request with:
   - Clear title
   - Description of changes
   - Reference to related issues
   - Screenshots/videos of new features

## Development Guidelines

### Project Structure

```
app/src/main/
├── java/com/movielocations/
│   ├── activities/    # UI screens
│   ├── adapters/      # RecyclerView adapters
│   ├── models/        # Data models
│   ├── database/      # Room DAOs
│   └── utils/         # Helper classes
└── res/
    ├── layout/        # XML layouts
    ├── values/        # Strings, colors, themes
    └── menu/          # Navigation menus
```

### Adding a New Feature

1. **Model** - Create data model in `models/`
2. **DAO** - Add database interface in `database/`
3. **Layout** - Design XML layout in `res/layout/`
4. **Activity/Adapter** - Implement logic
5. **Resources** - Add strings to `res/values/strings.xml`
6. **Test** - Verify on device/emulator

### Code Review Checklist

- [ ] Code is clean and readable
- [ ] No duplicate code
- [ ] Error handling in place
- [ ] Permissions requested when needed
- [ ] Memory leaks prevented (executors closed)
- [ ] Accessibility considered
- [ ] Works in portrait and landscape
- [ ] Tested on API 24+ devices

## Areas Needing Help

### Priority Features
- [ ] TMDb API integration
- [ ] User authentication
- [ ] Cloud synchronization
- [ ] Photo upload from locations
- [ ] Reviews and ratings system
- [ ] Search filters (by genre, year, etc.)

### Nice to Have
- [ ] Augmented Reality for locations
- [ ] Offline map caching
- [ ] Location recommendations
- [ ] Watch list functionality
- [ ] Multi-language UI support
- [ ] Dark theme support

### Bug Fixes
- Check GitHub Issues for bugs marked "good first issue"

## Testing

### Manual Testing
1. Install on physical device
2. Test each feature:
   - Browse movies
   - Search and filter
   - View details
   - Open map
   - Use GPS
   - Add friends
   - Send invitations
   - Share on social media

### Automated Testing
- Unit tests welcome but not required
- Follow existing test structure if present

## Documentation

When adding features:
- Update README.md with new features
- Add to FEATURES.md if significant
- Update SETUP.md if setup changes
- Add code comments

## Questions?

- Open a GitHub Discussion
- Comment on relevant Issues
- Check existing documentation

## License

By contributing, you agree that your contributions will be licensed under the same license as the project (MIT License).

## Recognition

Contributors will be listed in the project README.

Thank you for making Movie Locations better! 🎬📍
