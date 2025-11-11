# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

The **ZeroBias Compliance Feature Repository** contains compliance feature definitions that describe capabilities products/services provide for meeting regulatory requirements. Compliance features enable product comparison, requirement mapping, and vendor selection based on compliance capabilities.

**Repository Role:** Compliance Features Catalog - Product capability definitions for compliance

Compliance features are published as NPM packages and loaded into the platform via the dataloader to enable product-to-requirement mapping and compliance gap analysis.

## Architecture

### Compliance Feature Concept

**Compliance Features** describe **WHAT** a product can do to support compliance:
- Two-Factor Authentication (2FA)
- Audit Logging
- Data Encryption
- Access Control Management
- Vulnerability Scanning
- etc.

**Purpose:**
- Catalog product capabilities
- Map features to compliance requirements
- Enable product comparison
- Support vendor selection decisions
- Identify compliance gaps

### Repository Structure

```
compliance_feature/
├── package/zerobias/              # All compliance feature packages
│   ├── f_2fa/                     # Feature: Two-Factor Authentication
│   ├── f_al/                      # Feature: Audit Logging
│   ├── f_de/                      # Feature: Data Encryption
│   ├── f_acm/                     # Feature: Access Control Management
│   └── [140+ more features]
├── templates/                      # Templates for new features
├── scripts/                        # Automation scripts
│   ├── createNewCompliancefeature.sh  # Create new feature from template
│   ├── validate.ts                # Validate feature structure
│   ├── publish.sh                 # Publish feature to NPM
│   └── correctDeps.ts             # Fix dependencies
├── complianceFeatureTypes/        # Feature type definitions
├── bundle/                        # Bundle artifacts
└── lerna.json                     # Monorepo configuration
```

## Compliance Feature Package Structure

Each compliance feature is an NPM package:

```
package/zerobias/f_2fa/
├── index.yml                  # Feature metadata
├── elements.yml               # Linked compliance requirements
├── package.json               # NPM package definition
├── npm-shrinkwrap.json        # Locked dependencies
└── CHANGELOG.md               # Version history
```

### index.yml Format

```yaml
id: de9063a3-8455-48e1-87c1-fe30cc568970  # UUID
name: Two-Factor Authentication (2FA)      # Display name
description: Two-Factor Authentication (2FA)  # Description
imageUrl: https://cdn.auditmation.io/logos/zerobias-f_2fa-compliance_feature.svg
code: f_2fa                                # Package code
externalId: Two-Factor Authentication      # External identifier
tags: []
aliases: []                                # Alternative names
```

### elements.yml Format

Links compliance features to specific compliance requirements:

```yaml
# Link to framework requirements by standard alias and element alias
elements:
  - standardAlias: aicpa.soc2.2022.framework  # SOC 2 Framework
    elementAlias: CC6.1                        # Control: Logical Access
  - standardAlias: nist.csf.1.1.framework      # NIST CSF
    elementAlias: PR.AC-1                      # Identity Management
  - id: 870cb798-bba7-465c-adcd-323003cbc2f9  # Or link by requirement ID
```

**Purpose:**
- Map features to compliance requirements
- Show which controls a feature supports
- Enable automated compliance mapping
- Support gap analysis

### package.json Format

```json
{
  "name": "@zerobias-org/compliance_feature-zerobias-f_2fa",
  "version": "1.0.1",
  "description": "Two-Factor Authentication (2FA) compliance_feature artifact.",
  "author": "team@zerobias.com",
  "license": "ISC",
  "repository": {
    "type": "git",
    "url": "git@github.com:zerobias-org/compliance_feature.git",
    "directory": "package/zerobias/f_2fa/"
  },
  "publishConfig": {
    "registry": "https://pkg.zerobias.org/"
  },
  "files": [
    "index.yml",
    "elements.yml"
  ],
  "auditmation": {
    "dataloader-version": "4.0.31",
    "import-artifact": "compliance_feature",
    "package": "zerobias.f_2fa.compliance_feature"
  },
  "dependencies": {
    "@auditlogic/vendor-zerobias": "latest"
  }
}
```

## Naming Conventions

### Feature Codes

Format: `f_{abbreviation}[_{increment}]`

**Examples:**
- `f_2fa` - Two-Factor Authentication
- `f_al` - Audit Logging
- `f_de` - Data Encryption
- `f_acm` - Access Control Management
- `f_vs` - Vulnerability Scanning

**Abbreviation Rules:**
- Use first letter of each word in lowercase
- Example: "Two-Factor Authentication" → `2fa`
- Example: "Access Control Management" → `acm`
- If duplicate exists, add increment: `acm_2`, `acm_3`

### Package Names

Format: `@zerobias-org/compliance_feature-zerobias-{code}`

**Examples:**
- `@zerobias-org/compliance_feature-zerobias-f_2fa`
- `@zerobias-org/compliance_feature-zerobias-f_al`

## Development Workflow

### Creating a New Compliance Feature

**1. Set up environment:**
```bash
# Set ZB_TOKEN for npm authentication
export ZB_TOKEN="your-api-key"

# Clone and install
git clone git@github.com:zerobias-org/compliance_feature.git
cd compliance_feature
git checkout dev  # Always work on dev branch
npm install
```

**2. Create feature folder:**
```bash
cd package/zerobias

# Create folder with naming convention
# Example: f_sso (Single Sign-On)
mkdir f_sso
```

**3. Run creation script:**
```bash
# From repository root
sh scripts/createNewCompliancefeature.sh package/zerobias/f_sso
```

This copies templates and creates:
- `index.yml` (with placeholders)
- `elements.yml` (empty, for requirement mapping)
- `package.json` (with placeholders)

**4. Edit index.yml:**
```yaml
id: <generate-new-uuid>  # Use uuidgen or online generator
name: Single Sign-On (SSO)
description: Single Sign-On authentication capability
code: f_sso
externalId: Single Sign-On
tags: []
aliases:
  - SSO
  - Single Sign On
```

**5. Edit elements.yml (link to requirements):**
```yaml
elements:
  - standardAlias: aicpa.soc2.2022.framework
    elementAlias: CC6.1  # Logical and Physical Access Controls
  - standardAlias: nist.800-53.rev5.framework
    elementAlias: IA-2    # Identification and Authentication
```

**6. Edit package.json:**
```json
{
  "name": "@zerobias-org/compliance_feature-zerobias-f_sso",
  "version": "0.0.0",  // Start at 0.0.0
  "description": "Single Sign-On (SSO) compliance_feature artifact.",
  ...
  "dependencies": {
    "@auditlogic/vendor-zerobias": "latest"
  }
}
```

**7. Install and shrinkwrap:**
```bash
cd package/zerobias/f_sso
npm install
npm shrinkwrap
```

**8. Validate:**
```bash
# From repository root
npm run validate

# Fix any errors and re-validate
```

**9. Commit:**
```bash
git add package/zerobias/f_sso
git commit -m "feat(compliance_feature): add Single Sign-On feature"
git push origin dev
```

**10. Open Pull Request:**
- Create PR against `dev` branch (not main!)
- PR will run validation checks
- After review and merge, feature will be published automatically

---

## Version Management

### Lerna Versioning

Compliance features use **Lerna** with **Conventional Commits** for automatic versioning (same as segments):

**Version Bumps:**
- `feat:` commits → minor version bump (0.1.0 → 0.2.0)
- `fix:` commits → patch version bump (0.1.0 → 0.1.1)
- `BREAKING CHANGE:` → major version bump (0.1.0 → 1.0.0)

**Initial Version:**
- All new features start at `0.0.0`
- First commit bumps to `0.1.0` or `0.0.1`

**Graduating to 1.0.0:**
1. Assign PR to `premajor` label
2. Lerna creates release candidate (`1.0.0-rc.0`)
3. Merge PR
4. Lerna graduates to `1.0.0`

### Dry Run

```bash
npm run lerna:dry-run
```

---

## Publishing Workflow

### Automatic Publishing (via GitHub Actions)

**Trigger:** Merge to `main` branch

**Workflow:**
1. `lerna_publish.yml` runs
2. Lerna detects changed features
3. Bootstraps dependencies
4. Bumps versions based on conventional commits
5. Updates CHANGELOG.md
6. Publishes to ZeroBias NPM registry
7. Creates git tags
8. Triggers `lerna_post_publish.yml`
9. Sends Slack notification

---

## Integration with Platform

### Dataloader Import

Compliance features are imported into AuditgraphDB via dataloader:

**Import Process:**
1. Feature package published to NPM
2. Dataloader detects new version
3. Downloads package
4. Parses `index.yml` and `elements.yml`
5. Creates/updates ComplianceFeature object in AuditgraphDB
6. Links to compliance requirements (via elements.yml)
7. Updates product associations

**Dataloader Configuration:**
```json
{
  "import-artifact": "compliance_feature",
  "package": "zerobias.f_2fa.compliance_feature",
  "dataloader-version": "4.0.31"
}
```

### Product Association

Products declare which compliance features they support:

**Example Product (product/index.yml):**
```yaml
name: Okta Identity Cloud
complianceFeatures:
  - f_2fa    # Two-Factor Authentication
  - f_sso    # Single Sign-On
  - f_mfa    # Multi-Factor Authentication
  - f_rbac   # Role-Based Access Control
```

### Requirement Mapping

Compliance features link products to requirements:

**Query Flow:**
1. User searches: "Products that support SOC 2 CC6.1"
2. Platform queries compliance features linked to CC6.1
3. Returns products that implement those features
4. Shows compliance coverage for each product

---

## Common Development Commands

### Root Level

```bash
# Install dependencies
npm install

# Validate all features
npm run validate

# Dry run versioning
npm run lerna:dry-run

# Publish features
npm run lerna:publish
```

### Feature-Specific

```bash
cd package/zerobias/f_2fa

# Install dependencies
npm install

# Create shrinkwrap
npm shrinkwrap

# Validate feature
npm run validate

# Publish (usually via lerna)
npm run nx:publish
```

---

## Best Practices

### Creating Features

1. **Check for duplicates:** Search existing features before creating new ones
2. **Clear names:** Use industry-standard terminology
3. **Good descriptions:** Explain what the feature enables
4. **Link requirements:** Always populate elements.yml with relevant requirements
5. **Add aliases:** Include common alternative names

### Linking Requirements

1. **Be specific:** Link to precise requirement elements, not entire frameworks
2. **Use standardAlias:** Preferred over direct UUIDs for maintainability
3. **Multiple mappings:** Link to all relevant frameworks (SOC 2, ISO 27001, NIST, etc.)
4. **Verify requirements exist:** Ensure linked standards/elements are published

### Version Control

1. **Work on dev branch:** Never commit directly to main
2. **Conventional commits:** Always use proper commit format
3. **One feature per commit:** Don't mix multiple feature changes
4. **Test before PR:** Run `npm run validate` before pushing

---

## Common Issues and Solutions

### Validation Fails

**Problem:** `npm run validate` shows errors

**Solutions:**
1. Check index.yml format (YAML syntax)
2. Verify all required fields present
3. Check UUID format is valid
4. Verify code follows naming convention
5. Check elements.yml format

### Element Links Broken

**Problem:** Dataloader can't find linked requirements

**Solutions:**
1. Verify standard alias exists (e.g., aicpa.soc2.2022.framework)
2. Check element alias is correct (e.g., CC6.1)
3. Ensure standard package is published
4. Use requirement UUID if alias isn't working

### Commit Rejected

**Problem:** Husky pre-commit hook fails

**Solutions:**
1. Fix commit message format
2. Run `npm run validate` first
3. Check for linting errors

---

## Related Documentation

- **[Root CLAUDE.md](../../CLAUDE.md)** - Meta-repo guidance
- **[ContentArtifacts.md](../../ContentArtifacts.md)** - Content catalog system
- **[zerobias-org/product/CLAUDE.md](../product/CLAUDE.md)** - Product definitions
- **[zerobias-org/segment/CLAUDE.md](../segment/CLAUDE.md)** - Product segments
- **[auditmation/platform/dataloader/CLAUDE.md](../../auditmation/platform/dataloader/CLAUDE.md)** - Dataloader import
- **[README.md](README.md)** - Repository overview
- **[Conventional Commits](https://www.conventionalcommits.org/)** - Commit message format

---

## Support

For compliance feature development:
1. Review existing features for examples
2. Check validation errors carefully
3. Follow naming conventions
4. Test with dry-run before publishing
5. Ask in community channels for help

---

**Last Updated:** 2025-11-11
**Maintainers:** ZeroBias Community
