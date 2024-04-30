# sdip-error-codes

Common lib containing SDIP error codes

# Usage

## Add submodule

In order to add the module to your current project, run the command

    git submodule add <relative-path-to-sdip-error-codes.git> <desired/path/in/project>

Relative mapping needs to be used in order for the gitlab-runner to be able to clone the repo

### Example

    git submodule add https://github.com/scania/sdos-error-code.git errorcode/sdos-error-code

Run the command in the root of the parent repo, you should now have a new `.gitmodules` file in the
root of your service repo, with the following content

    [submodule "errorcode/sdos-error-code"]
	  path = errorcode/sdos-error-code
	  url = https://github.com/scania/sdos-error-code

## Update submodule

To update the submodule run (in repo root)

    git submodule update --init --recursive

## Gradle build using  including a submodule

Now the library can be found in `<repo-root>/common-sdip/sdip-error-codes` and may be included in
the project by adding

    include 'errorcode:sdos-error-code'

in settings.gradle and adding

    implementation project(':errorcode:sdos-error-code')

to build.gradle


