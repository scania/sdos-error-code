# sdip-error-codes

Common lib containing SDIP error codes

# Usage

## Add submodule

In order to add the module to your current project, run the command

    git submodule add <relative-path-to-sdip-error-codes.git> <desired/path/in/project>

Relative mapping needs to be used in order for the gitlab-runner to be able to clone the repo

### Example

    git submodule add ../common/sdip-error-codes.git common-sdip/sdip-error-codes

Run the command in the root of the parent repo, you should now have a new `.gitmodules` file in the
root of your service repo, with the following content

    [submodule "common-sdip/sdip-error-codes"]
	  path = common-sdip/sdip-error-codes
	  url = ../common/sdip-error-codes.git

## Update submodule

To update the submodule run (in repo root)

    git submodule update --init --recursive

## Gradle build using  including a submodule

Now the library can be found in `<repo-root>/common-sdip/sdip-error-codes` and may be included in
the project by adding

    include 'common-sdip:sdip-error-codes'

in settings.gradle and adding

    implementation project(':common-sdip:sdip-error-codes')

to build.gradle

## Gitlab pipeline update

In order for the gitlab pipeline to work, the following variable needs to be set in
the `.gitlab-ci.yml`

    # Make sure submodule is updated before running the jobs
    variables:
        GIT_SUBMODULE_STRATEGY: recursive

